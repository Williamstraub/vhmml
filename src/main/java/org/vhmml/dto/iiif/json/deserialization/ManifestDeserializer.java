package org.vhmml.dto.iiif.json.deserialization;

import java.io.IOException;

import org.vhmml.dto.iiif.IIIFTypedMetaData;
import org.vhmml.dto.iiif.Manifest;
import org.vhmml.dto.iiif.SeeAlso;
import org.vhmml.dto.iiif.Sequence;
import org.vhmml.util.JacksonUtil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ManifestDeserializer extends JsonDeserializer<Manifest> {
	
	@Override
	public Manifest deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Manifest manifest = new Manifest();
		JsonNode node = jp.getCodec().readTree(jp);
		manifest.setContext(JacksonUtil.getStringProperty(node, "@context"));
		manifest.setId(JacksonUtil.getStringProperty(node, "@id"));
		manifest.setType(JacksonUtil.getStringProperty(node, "@type"));
		manifest.setLabel(JacksonUtil.getStringProperty(node, "label"));
		manifest.setDescription(JacksonUtil.getStringProperty(node, "description"));
		manifest.setAttribution(JacksonUtil.getStringProperty(node, "attribution"));
		manifest.setSeeAlso(JacksonUtil.getObjectProperty(node, "seeAlso", SeeAlso.class));	
		manifest.setSequences(JacksonUtil.getListProperty(node, "sequences", Sequence.class));
		manifest.setDcTermsWithin(JacksonUtil.getStringProperty(node, "dcterms:within"));
		manifest.setScMetaData(JacksonUtil.getObjectProperty(node, "sc:metadata", IIIFTypedMetaData.class));
		
		return manifest;
	}
}
