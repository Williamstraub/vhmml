package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Service;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ServiceDeserializer extends JsonDeserializer<Service> {

	@Override
	public Service deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Service service = new Service();
		JsonNode node = jp.getCodec().readTree(jp);
		
		service.setId(JacksonUtil.getStringProperty(node, "@id"));
		service.setContext(JacksonUtil.getStringProperty(node, "@context"));
		service.setProfile(JacksonUtil.getStringProperty(node, "profile"));
		
		return service;
	}
}
