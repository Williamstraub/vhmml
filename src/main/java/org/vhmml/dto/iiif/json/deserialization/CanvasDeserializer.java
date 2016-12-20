package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Canvas;
import org.vhmml.dto.iiif.Image;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class CanvasDeserializer extends JsonDeserializer<Canvas> {

	@Override
	public Canvas deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Canvas canvas = new Canvas();
		JsonNode node = jp.getCodec().readTree(jp);
		
		canvas.setId(JacksonUtil.getStringProperty(node, "@id"));
		canvas.setType(JacksonUtil.getStringProperty(node, "@type"));
		canvas.setLabel(JacksonUtil.getStringProperty(node, "label"));
		canvas.setHeight(JacksonUtil.getIntegerProperty(node, "height"));
		canvas.setWidth(JacksonUtil.getIntegerProperty(node, "width"));		
		canvas.setImages(JacksonUtil.getListProperty(node, "images", Image.class));
		
		return canvas;
	}
}
