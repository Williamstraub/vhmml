package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Manifest;
import org.vhmml.dto.iiif.Sequence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ManifestSerializer extends JsonSerializer<Manifest> {
 
    @Override
    public void serialize(Manifest manifest, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();
	    	jsonGenerator.writeStringField("@context", manifest.getContext());
	    	jsonGenerator.writeStringField("@id", manifest.getId());
	    	jsonGenerator.writeStringField("@type", manifest.getType());
	    	jsonGenerator.writeStringField("label", manifest.getLabel());
	    	jsonGenerator.writeStringField("location", "vhmml");
	    	jsonGenerator.writeStringField("logo", "http://localhost:8080/vhmml/static/img/hmml-logo.png");	    	
	    	jsonGenerator.writeStringField("description", manifest.getDescription());
	    	jsonGenerator.writeStringField("attribution", manifest.getAttribution());
	    	jsonGenerator.writeObjectField("seeAlso", manifest.getSeeAlso());
	    	
	    	jsonGenerator.writeArrayFieldStart("sequences");
	    	
	    	for(Sequence sequence : manifest.getSequences()) {
	    		jsonGenerator.writeObject(sequence);
	    	}

	    	jsonGenerator.writeEndArray();
	    	jsonGenerator.writeStringField("dcterms:within", manifest.getDcTermsWithin());
	    	jsonGenerator.writeObjectField("sc:metadata", manifest.getScMetaData());
    	jsonGenerator.writeEndObject();
    }
}
