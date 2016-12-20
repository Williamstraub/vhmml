package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Canvas;
import org.vhmml.dto.iiif.Image;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CanvasSerializer extends JsonSerializer<Canvas> {
 
    @Override
    public void serialize(Canvas canvas, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();    	
    		jsonGenerator.writeStringField("@id", canvas.getId());
    		jsonGenerator.writeStringField("@type", canvas.getType());
    		jsonGenerator.writeStringField("label", canvas.getLabel());
    		jsonGenerator.writeNumberField("height", canvas.getHeight());
    		jsonGenerator.writeNumberField("width", canvas.getWidth());
    		jsonGenerator.writeArrayFieldStart("images");
    		
    		for(Image image : canvas.getImages()) {
    			jsonGenerator.writeObject(image);
    		}
    		
    		jsonGenerator.writeEndArray();

    	jsonGenerator.writeEndObject();
    }
}
