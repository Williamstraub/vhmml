package org.vhmml.util;

import java.io.IOException;

import org.vhmml.entity.Tag;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TagSerializer extends JsonSerializer<Tag> {
	
	public void serialize(Tag tag, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {     
      generator.writeString(tag.getTag());      
	}
}
