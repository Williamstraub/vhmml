package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.SeeAlso;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SeeAlsoSerializer extends JsonSerializer<SeeAlso> {
 
    @Override
    public void serialize(SeeAlso seeAlso, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();
    		jsonGenerator.writeStringField("@id", seeAlso.getId());
    		jsonGenerator.writeStringField("dcterms:format", seeAlso.getDcTermsFormat());
    	jsonGenerator.writeEndObject();
    }
}
