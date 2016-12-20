package org.vhmml.util;

import java.io.IOException;

import org.vhmml.entity.ReferenceEntry.DisplayField;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DisplayFieldSerializer extends JsonSerializer<DisplayField> {
	
	public void serialize(DisplayField displayField, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
      generator.writeStartObject();
      generator.writeFieldName("name");
      generator.writeString(displayField.getName());
      generator.writeFieldName("label");
      generator.writeString(displayField.getLabel());
      generator.writeEndObject();
  }
}
