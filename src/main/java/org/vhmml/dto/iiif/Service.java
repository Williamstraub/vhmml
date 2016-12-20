package org.vhmml.dto.iiif;

import org.vhmml.dto.iiif.json.deserialization.ServiceDeserializer;
import org.vhmml.dto.iiif.json.serialization.ServiceSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ServiceSerializer.class)
@JsonDeserialize(using = ServiceDeserializer.class)
public class Service extends IIIFIdentifiable {

	private String context;
	private String profile;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
