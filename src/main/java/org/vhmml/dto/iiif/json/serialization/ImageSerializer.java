package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Image;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ImageSerializer extends JsonSerializer<Image> {

    @Override
    public void serialize(Image image, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();    	
    		jsonGenerator.writeStringField("@id", image.getId());
    		jsonGenerator.writeStringField("@type", image.getType());
    		jsonGenerator.writeStringField("motivation", image.getMotivation());
    		jsonGenerator.writeObjectField("resource", image.getResource());
    		jsonGenerator.writeStringField("on", image.getOn());    		
    	jsonGenerator.writeEndObject();
    }
}
