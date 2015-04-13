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
package org.n52.flickr.model;

import java.util.Date;

import org.joda.time.DateTime;

public class FlickrQuery {

	private String[] keywords;
	private double minimumLongitude = Double.MIN_VALUE;
	private double minimumLatitude = Double.MIN_VALUE;
	private double maximumLongitude = Double.MIN_VALUE;
	private double maximumLatitude = Double.MIN_VALUE;
	private DateTime minimalDate;
	private DateTime maximalDate;

	public FlickrQuery(double minimumLongitude,
			double minimumLatitude,
			double maximumLongitude,
			double maximumLatitude,
			DateTime start,
			DateTime end,
			String[] tags) {
		this.minimumLongitude = minimumLongitude;
		this.minimumLatitude = minimumLatitude;
		this.maximumLongitude = maximumLongitude;
		this.maximumLatitude = maximumLatitude;
		minimalDate = start;
		maximalDate = end;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public String getMinLon() {
		return Double.toString(minimumLongitude);
	}

	public String getMinLat() {
		return Double.toString(minimumLatitude);
	}

	public String getMaxLon() {
		return Double.toString(maximumLongitude);
	}

	public String getMaxLat() {
		return Double.toString(maximumLatitude);
	}

	public Date getMinDate() {
		return minimalDate.toDate();
	}

	public Date getMaxDate() {
		return maximalDate.toDate();
	}

	public boolean hasKeywords() {
		return keywords != null &&
				keywords.length > 0;
	}

	public boolean isGeolocated() {
		return minimumLatitude != Double.MIN_VALUE &&
				minimumLongitude != Double.MIN_VALUE &&
				maximumLatitude != Double.MIN_VALUE &&
				maximumLongitude != Double.MIN_VALUE;
	}

	public boolean hasMinDate() {
		return minimalDate != null;
	}

	public boolean hasMaxDate() {
		return maximalDate != null;
	}

}
