package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ResourceSerializer extends JsonSerializer<Resource> {
 
    @Override
    public void serialize(Resource resource, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();    	
    		jsonGenerator.writeStringField("@id", resource.getId());
    		jsonGenerator.writeStringField("@type", resource.getType());
    		jsonGenerator.writeStringField("format", resource.getFormat());
    		jsonGenerator.writeNumberField("height", resource.getHeight());
    		jsonGenerator.writeNumberField("width", resource.getWidth());
    		jsonGenerator.writeObjectField("service", resource.getService());
    	jsonGenerator.writeEndObject();
    }
}
