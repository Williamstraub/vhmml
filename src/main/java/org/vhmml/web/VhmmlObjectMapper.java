package org.vhmml.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class VhmmlObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = -8216004414226411221L;

	public VhmmlObjectMapper() {
		this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
}
