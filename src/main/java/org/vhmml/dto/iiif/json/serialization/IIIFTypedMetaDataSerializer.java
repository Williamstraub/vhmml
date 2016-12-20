package org.vhmml.dto.iiif.json.serialization;

import java.io.IOException;

import org.vhmml.dto.iiif.IIIFTypedMetaData;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class IIIFTypedMetaDataSerializer extends JsonSerializer<IIIFTypedMetaData> {

    @Override
    public void serialize(IIIFTypedMetaData metaData, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
    	jsonGenerator.writeStartObject();
    		jsonGenerator.writeStringField("@id", metaData.getId());
    		jsonGenerator.writeStringField("@type", metaData.getType());
    	jsonGenerator.writeEndObject();
    }
}
