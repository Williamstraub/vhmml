package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Image;
import org.vhmml.dto.iiif.Resource;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ImageDeserializer extends JsonDeserializer<Image> {

	@Override
	public Image deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Image image = new Image();
		JsonNode node = jp.getCodec().readTree(jp);		
		
		image.setId(JacksonUtil.getStringProperty(node, "@id"));
		image.setType(JacksonUtil.getStringProperty(node, "@type"));
		image.setMotivation(JacksonUtil.getStringProperty(node, "motivation"));
		image.setResource(JacksonUtil.getObjectProperty(node, "resource", Resource.class));		
		image.setOn(JacksonUtil.getStringProperty(node, "on"));		
		
		return image;
	}
}
