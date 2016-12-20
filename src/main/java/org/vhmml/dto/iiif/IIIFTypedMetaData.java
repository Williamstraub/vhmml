package org.vhmml.dto.iiif;

import org.vhmml.dto.iiif.json.deserialization.IIIFTypedMetaDataDeserializer;
import org.vhmml.dto.iiif.json.serialization.IIIFTypedMetaDataSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = IIIFTypedMetaDataSerializer.class)
@JsonDeserialize(using = IIIFTypedMetaDataDeserializer.class)
public class IIIFTypedMetaData extends IIIFIdentifiable {

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
