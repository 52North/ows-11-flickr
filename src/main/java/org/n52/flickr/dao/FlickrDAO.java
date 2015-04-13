/**
 * ﻿Copyright (C) 2015 - 2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * license version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.flickr.dao;

import java.util.Collection;
import java.util.LinkedList;

import org.n52.flickr.FlickrHarvester;
import org.n52.flickr.model.AccessToken;
import org.n52.flickr.model.FlickrLocation;
import org.n52.flickr.model.FlickrMessage;
import org.n52.flickr.model.FlickrQuery;
import org.n52.socialmedia.DecodingException;
import org.n52.socialmedia.model.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.geo.GeoInterface;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class FlickrDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlickrDAO.class);

	private Flickr flickr;

	private String sharedSecret;
	
	public FlickrDAO(AccessToken accessToken, String token, String tokenSecret) {
		flickr = new Flickr(accessToken.getApiKey(), accessToken.getSharedSecret(), new REST(/*FlickrHarvester.API_HOST, FlickrHarvester.API_PORT*/));
		sharedSecret = accessToken.getSharedSecret();
		Flickr.debugStream = false;
		Flickr.debugRequest = false;
		
		Auth auth = new Auth();
		auth.setPermission(Permission.READ);
		auth.setToken(token);
		auth.setTokenSecret(tokenSecret);
		
		RequestContext requestContext = RequestContext.getRequestContext();
	    requestContext.setAuth(auth);
	}
	
	/**
	 * @throws DecodingException 
	 * @throws FlickrException 
	 */
	public Collection<FlickrMessage> executeQuery(FlickrQuery query) throws DecodingException, FlickrException {
		LinkedList<FlickrMessage> flickrPhotos = new LinkedList<>(); 
        
        PhotosInterface photoInterface = flickr.getPhotosInterface();

        SearchParameters params = new SearchParameters();
        if (query.hasKeywords()){
        	params.setTags(query.getKeywords());
        	params.setTagMode("all");
        }
        if (query.isGeolocated()){
        	params.setHasGeo(true);
        	params.setBBox(query.getMinLon(), query.getMinLat(), query.getMaxLon(), query.getMaxLat());
        }
        if (query.hasMinDate()){
        	params.setMinTakenDate(query.getMinDate());
        }
        if (query.hasMaxDate()) {
        	params.setMaxTakenDate(query.getMaxDate());
        }

        int pageIndex = 1;
        int pages = 1;
        do {
            PhotoList<Photo> photoList = photoInterface.search(params, 500, pageIndex);
            pages = photoList.getPages();

            LOGGER.info("number of photos on page {}: {} of {} photos for this result set.",
                    pageIndex,
                    photoList.size(),
                    photoList.getTotal());

            for (int i = 0; i < photoList.size(); i++) {
                Photo photo = (Photo) photoList.get(i);

                // careful, this call takes long:
                FlickrMessage flickrMessage = createFlickrMessage(photo);
                if (flickrMessage != null) {
                	LOGGER.info("Downloaded photo No. {}.: {}", i, flickrMessage);

                	flickrPhotos.add(flickrMessage);
                } else {
                	LOGGER.info("Downloaded photo No. {}, but not geo located, hence skipped",
                			i);
                }
            }

            pageIndex++;

        } while (pageIndex < pages);

        return flickrPhotos;
	}

	private FlickrMessage createFlickrMessage(Photo photo) throws FlickrException {
		
		FlickrMessage flickrMessage = new FlickrMessage();
        PhotosInterface photoInterface = flickr.getPhotosInterface();
        GeoInterface geoInterface = flickr.getGeoInterface();
        String photoID = photo.getId();
        flickrMessage.setIdentifier(photoID);
        
        // geo:
        GeoData photoGeo = geoInterface.getLocation(photoID);
        if (photoGeo != null) {

        	// photo info:
        	Photo photoInfo = photoInterface.getInfo(photoID, sharedSecret);
        	if (photoInfo.getDatePosted() == null) {
        		return null;
        	}
        	flickrMessage.setDatePosted(photoInfo.getDatePosted());
        	if (photoInfo.getDateTaken() == null) {
        		flickrMessage.setDateTaken(photoInfo.getDatePosted());
        	} else {
        		flickrMessage.setDateTaken(photoInfo.getDateTaken());
        	}
        	flickrMessage.setLocation(new FlickrLocation(photoGeo.getLongitude(),
        			photoGeo.getLatitude(),
        			photoGeo.getAccuracy(),
        			photoInfo.getLocality()));
        	flickrMessage.setTitle(photoInfo.getTitle());
        	flickrMessage.setUrl(photoInfo.getUrl());
        	flickrMessage.setCaption(photoInfo.getDescription());
        	flickrMessage.setTags(photoInfo.getTags());

        	// user:
        	User user = photoInfo.getOwner();
        	flickrMessage.setProcedure(new Procedure(user.getUsername(), user.getProfileurl()));

        	return flickrMessage;

        } else {
        	return null;
        }
	}
	
}
