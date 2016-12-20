package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Canvas;
import org.vhmml.dto.iiif.Sequence;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class SequenceDeserializer extends JsonDeserializer<Sequence> {

	@Override
	public Sequence deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Sequence sequence = new Sequence();
		JsonNode node = jp.getCodec().readTree(jp);
		
		sequence.setId(JacksonUtil.getStringProperty(node, "@id"));
		sequence.setType(JacksonUtil.getStringProperty(node, "@type"));
		sequence.setLabel(JacksonUtil.getStringProperty(node, "label"));		
		sequence.setCanvases(JacksonUtil.getListProperty(node, "canvases", Canvas.class));
		
		return sequence;
	}
}
