package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Resource;
import org.vhmml.dto.iiif.Service;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ResourceDeserializer extends JsonDeserializer<Resource> {
	
	@Override
	public Resource deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Resource resource = new Resource();
		JsonNode node = jp.getCodec().readTree(jp);
		resource.setId(JacksonUtil.getStringProperty(node, "@id"));
		resource.setType(JacksonUtil.getStringProperty(node, "@type"));
		resource.setFormat(JacksonUtil.getStringProperty(node, "format"));
		resource.setHeight(JacksonUtil.getIntegerProperty(node, "height"));
		resource.setWidth(JacksonUtil.getIntegerProperty(node, "width"));
		resource.setService(JacksonUtil.getObjectProperty(node, "service", Service.class));
		
		return resource;
	}
}
