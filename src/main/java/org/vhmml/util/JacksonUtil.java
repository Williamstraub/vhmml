package org.vhmml.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.lang3.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;

public class JacksonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String getStringProperty(JsonNode node, String propertyName) {
		
		String value = null;
		
		if(node.has(propertyName)) {
			value = node.get(propertyName).asText().trim();
		}
		
		return value;	
	}
	
	public static Boolean getBooleanProperty(JsonNode node, String propertyName) {
		
		Boolean value = null;
		
		if(node.has(propertyName)) {
			value = node.get(propertyName).booleanValue();
		}
		
		return value;	
	}
	
	public static Integer getIntegerProperty(JsonNode node, String propertyName) {
		
		Integer value = null;
		
		if(node.has(propertyName)) {
			value = (Integer)((IntNode)node.get(propertyName)).numberValue();
		}
		
		return value;	
	}
	
	public static <T> T getObjectProperty(JsonNode node, String propertyName, Class<T> type) throws IOException {
		T object = null;
		
		if(node.has(propertyName)) {
			String propertyJson = node.get(propertyName).toString();
			
			if(StringUtils.isNotEmpty(propertyJson)) {
				object = mapper.readValue(propertyJson, type);
			}			
		}
		
		return object;
	}
	
	public static <T> List<T> getListProperty(JsonNode node, String propertyName, Class<T> type) throws IOException {		
		List<T> list = new ArrayList<T>();
		JsonNode listNode = node.get(propertyName);
		
		if(listNode != null && listNode.isArray() && listNode.size() > 0) {			

			for(JsonNode itemNode : listNode) {
				list.add(mapper.readValue(itemNode.toString(), type));
			}			
		}
		
		return list;
	}
}
