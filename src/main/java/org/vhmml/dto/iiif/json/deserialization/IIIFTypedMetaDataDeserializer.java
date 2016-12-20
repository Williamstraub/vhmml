package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.IIIFTypedMetaData;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class IIIFTypedMetaDataDeserializer extends JsonDeserializer<IIIFTypedMetaData> {

	@Override
	public IIIFTypedMetaData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		IIIFTypedMetaData metaData = new IIIFTypedMetaData();
		JsonNode node = jp.getCodec().readTree(jp);
		
		metaData.setId(JacksonUtil.getStringProperty(node, "@id"));
		metaData.setType(JacksonUtil.getStringProperty(node, "@type"));
		
		return metaData;
	}
}
