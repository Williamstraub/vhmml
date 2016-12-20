package org.vhmml.web.el.function;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class VhmmlElFunctions {

	public static String getCompleteFieldName(String fieldName, String listName, Integer listIndex) {
		String completeFieldName = fieldName;
		
		if(StringUtils.isNotEmpty(listName) && listIndex != null) {
			completeFieldName = listName + "[" + listIndex + "]";
			
			// if it's a list but they didn't pass a field name, then it's something simple like a list of strings, e.g. alternateSurrogateFormats
			if(StringUtils.isNotEmpty(fieldName)) {
				completeFieldName += "." + fieldName;
			}
		}
		
		return completeFieldName;
	}
	
	public static Integer getFieldCount(List<Object> list) {
		Integer count = 0;
		
		if(CollectionUtils.isNotEmpty(list)) {
			count = list.size() - 1;
		}
		
		return count;
	}
}
