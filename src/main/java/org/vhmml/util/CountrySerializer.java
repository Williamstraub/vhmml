package org.vhmml.util;

import java.io.IOException;

import org.vhmml.entity.readingroom.Country;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CountrySerializer extends JsonSerializer<Country> {
	
	public void serialize(Country country, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
		String countryName = country != null ? country.getName() : "";
		generator.writeString(countryName);		
	}
}
