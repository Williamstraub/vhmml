package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ServiceSerializer extends JsonSerializer<Service> {

    @Override
    public void serialize(Service service, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();
    		jsonGenerator.writeStringField("@id", service.getId());
    		jsonGenerator.writeStringField("@context", service.getContext());
    		jsonGenerator.writeStringField("profile", service.getProfile());
    	jsonGenerator.writeEndObject();
    }
}
