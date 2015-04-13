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
 * 
 * http://twitter4j.org/en/code-examples.html
 */
package org.n52.flickr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;

public class FlickrAccessTokenRetriever {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlickrHarvester.class);
	
	public static Properties props;

	public static void main(String args[]) throws Exception{
		String[] props = readProperties();
		
	    Flickr flickr = new Flickr(props[0], props[1], new REST());
	    Flickr.debugStream = false;
	    Flickr.debugRequest = false;
        AuthInterface authInterface = flickr.getAuthInterface();

        Scanner scanner = new Scanner(System.in);

        Token token = authInterface.getRequestToken();
        LOGGER.info("token: " + token);

        String url = authInterface.getAuthorizationUrl(token, Permission.READ);
        LOGGER.info("Follow this URL to authorise yourself on Flickr");
        LOGGER.info("URL: {}", url);
        LOGGER.info("Paste in the token it gives you:");
        LOGGER.info(">>");

        String tokenKey = scanner.nextLine();
        scanner.close();

        Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));
        LOGGER.info("Authentication success");

        // This token can be used until the user revokes it.
        LOGGER.info("Token: {}", requestToken.getToken());
        LOGGER.info("Secret: {}", requestToken.getSecret());
	    //persist to the accessToken for future reference.
	    storeAccessToken(requestToken.getToken(), requestToken.getSecret());
	  }
	
	private static String[] readProperties() {
		InputStream is = FlickrAccessTokenRetriever.class.getResourceAsStream(FlickrHarvester.FLICKR_CREDENTIALS_PROPERTIES);
		if (is == null) {
			throw new IllegalStateException(FlickrHarvester.FLICKR_CREDENTIALS_PROPERTIES + " file not found.");
		}
		
		props = new Properties();
		try {
			props.load(is);
			String accessToken = props.getProperty("ACCESS_TOKEN");
			String accessTokenSecret = props.getProperty("ACCESS_TOKEN_SECRET");
			return new String[] {accessToken, accessTokenSecret };
		} catch (IOException e) {
			LOGGER.warn("properties malformed or unreadable", e);
			throw new IllegalStateException(e);
		}
	}
	  private static void storeAccessToken(String token, String secret){
		  props.setProperty("OAUTH_CONSUMER_KEY", token);
		  props.setProperty("OAUTH_CONSUMER_SECRET", secret);
		  
		  try (OutputStream os = new FileOutputStream(new File ("src/test/resources" + FlickrHarvester.FLICKR_CREDENTIALS_PROPERTIES))) {
			  props.store(os, "Infos: QUADRATICAL_BBOX_WIDTH MUST be in m. SEARCH_TERMS MUST be a comma separated list. Do NOT upload this file to any remote ressource.");
		  } catch (FileNotFoundException e) {
			LOGGER.error("Could not find file!", e);
		} catch (IOException e) {
			LOGGER.error("Error while writing to file", e);
		}
	  }
	
}
