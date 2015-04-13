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

import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;
import org.n52.socialmedia.model.HumanVisualPerceptionObservation;
import org.n52.socialmedia.model.Procedure;
import org.n52.socialmedia.util.StringUtil;

import com.flickr4java.flickr.tags.Tag;

public class FlickrMessage implements HumanVisualPerceptionObservation {
	
	private String id;
	private FlickrLocation location;
	private DateTime createdTime;
	private DateTime postedTime;
	private String link;
	private Procedure procedure;
	private String caption;

	private Collection<Tag> tags;
	private String title;
	
	public FlickrMessage() {}
	
	@Override
	public FlickrLocation getLocation() {
		return location;
	}
	
	@Override
	public DateTime getPhenomenonTime() {
		return createdTime;
	}

	@Override
	public DateTime getResultTime() {
		return postedTime;
	}

	@Override
	public String getIdentifier() {
		return id;
	}

	@Override
	public Procedure getProcedure() {
		return procedure;
	}

	@Override
	public String getResultHref() {
		return link;
	}

	@Override
	public String getResult() {
		return StringUtil.escapeForXML(String.format("TITLE: %s; CAPTION: %s; TAGS: %s; ",
				title!=null?!title.isEmpty()?title:"title-not-set":"title-not-set",
				caption!=null?!caption.isEmpty()?caption:"caption-not-set":"caption-not-set",
				tags!=null?!tags.isEmpty()?tags:"tags-not-set":"tags-not-set"));
	}
	
	public void setIdentifier(String photoID) {
		id = photoID;
	}

	public void setLocation(FlickrLocation flickrLocation) {
		location = flickrLocation;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		link = url;
	}

	public void setCaption(String description) {
		caption = description;
	}

	public void setTags(Collection<Tag> tags) {
		this.tags = tags;
	}

	public void setDatePosted(Date datePosted) {
		postedTime = new DateTime(datePosted);
	}

	public void setDateTaken(Date dateTaken) {
		createdTime = new DateTime(dateTaken);
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}
	
	/*
	 * GENERATED with eclipse templates
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result
				+ ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((postedTime == null) ? 0 : postedTime.hashCode());
		result = prime * result
				+ ((procedure == null) ? 0 : procedure.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlickrMessage [getLocation()=").append(getLocation())
				.append(", getPhenomenonTime()=").append(getPhenomenonTime())
				.append(", getResultTime()=").append(getResultTime())
				.append(", getIdentifier()=").append(getIdentifier())
				.append(", getProcedure()=").append(getProcedure())
				.append(", getResultHref()=").append(getResultHref())
				.append(", getResult()=").append(getResult()).append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FlickrMessage other = (FlickrMessage) obj;
		if (caption == null) {
			if (other.caption != null) {
				return false;
			}
		} else if (!caption.equals(other.caption)) {
			return false;
		}
		if (createdTime == null) {
			if (other.createdTime != null) {
				return false;
			}
		} else if (!createdTime.equals(other.createdTime)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (link == null) {
			if (other.link != null) {
				return false;
			}
		} else if (!link.equals(other.link)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (postedTime == null) {
			if (other.postedTime != null) {
				return false;
			}
		} else if (!postedTime.equals(other.postedTime)) {
			return false;
		}
		if (procedure == null) {
			if (other.procedure != null) {
				return false;
			}
		} else if (!procedure.equals(other.procedure)) {
			return false;
		}
		if (tags == null) {
			if (other.tags != null) {
				return false;
			}
		} else if (!tags.equals(other.tags)) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		return true;
	}
	
}
