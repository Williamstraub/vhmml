package org.vhmml.dto.iiif;

import org.vhmml.dto.iiif.json.deserialization.ImageDeserializer;
import org.vhmml.dto.iiif.json.serialization.ImageSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ImageSerializer.class)
@JsonDeserialize(using = ImageDeserializer.class)
public class Image extends IIIFTypedMetaData {
	
	private String motivation;
	private Resource resource;
	private String on;

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getOn() {
		return on;
	}

	public void setOn(String on) {
		this.on = on;
	}
}
