package org.vhmml.util;

import java.io.IOException;

import org.vhmml.dto.zotero.ZoteroReferenceCreator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ZoteroReferenceCreatorDeserializer extends JsonDeserializer<ZoteroReferenceCreator> {

	public ZoteroReferenceCreatorDeserializer() {
		super();
	}

	@Override
	public ZoteroReferenceCreator deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ZoteroReferenceCreator creator = new ZoteroReferenceCreator();
		
		JsonNode node = jp.getCodec().readTree(jp);
		
		creator.setName(JacksonUtil.getStringProperty(node, "name"));
		creator.setFirstName(JacksonUtil.getStringProperty(node, "firstName"));
		creator.setLastName(JacksonUtil.getStringProperty(node, "lastName"));
		creator.setCreatorType(JacksonUtil.getStringProperty(node, "creatorType"));
		
		return creator;
	}
}
