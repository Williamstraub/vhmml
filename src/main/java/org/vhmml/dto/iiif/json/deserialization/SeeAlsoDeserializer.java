package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.SeeAlso;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class SeeAlsoDeserializer extends JsonDeserializer<SeeAlso> {

	@Override
	public SeeAlso deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		SeeAlso seeAlso = new SeeAlso();
		JsonNode node = jp.getCodec().readTree(jp);
		seeAlso.setId(JacksonUtil.getStringProperty(node, "@id"));
		seeAlso.setDcTermsFormat(JacksonUtil.getStringProperty(node, "dcterms:format"));			
		
		return seeAlso;
	}

}
