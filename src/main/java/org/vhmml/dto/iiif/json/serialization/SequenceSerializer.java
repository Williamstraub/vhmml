package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.Canvas;
import org.vhmml.dto.iiif.Sequence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SequenceSerializer extends JsonSerializer<Sequence> {
 
    @Override
    public void serialize(Sequence sequence, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();    	
    		jsonGenerator.writeStringField("@id", sequence.getId());
    		jsonGenerator.writeStringField("@type", sequence.getType());
    		jsonGenerator.writeStringField("label", sequence.getLabel());
    		jsonGenerator.writeArrayFieldStart("canvases");
    		
    		for(Canvas canvas : sequence.getCanvases()) {
    			jsonGenerator.writeObject(canvas);
    		}
    		
    		jsonGenerator.writeEndArray();

    	jsonGenerator.writeEndObject();
    }
}
