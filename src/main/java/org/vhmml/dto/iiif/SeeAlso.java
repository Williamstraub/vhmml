package org.vhmml.dto.iiif;

import org.vhmml.dto.iiif.json.deserialization.SeeAlsoDeserializer;
import org.vhmml.dto.iiif.json.serialization.SeeAlsoSerializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = SeeAlsoSerializer.class)
@JsonDeserialize(using = SeeAlsoDeserializer.class)
public class SeeAlso extends IIIFIdentifiable {

	private String dcTermsFormat;

	public String getDcTermsFormat() {
		return dcTermsFormat;
	}

	public void setDcTermsFormat(String dcTermsFormat) {
		this.dcTermsFormat = dcTermsFormat;
	}
}
