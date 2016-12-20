package org.vhmml.util;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.highlight.HighlightField;

public class ElasticSearchUtil {

	public ElasticSearchUtil() {
		super();
	}

	/**
	 * This method will retrieve a field value from a search hit, looking first to see if the field has a highlighted value, second
	 * to see if the "folded" version of the field has a highlighted value and finally, the normal field value if there is no
	 * highlighted value available.  The "folded" version of a field value is the version stored in the search index 
	 * that has diacritics removed and is changed to all lowercase.
	 *  
	 * @param searchHitSource
	 * @param highlightFields
	 * @param fieldName
	 * @return
	 */
	public static String getFieldValue(Map<String, Object> searchHitSource, Map<String, HighlightField> highlightFields, String fieldName) {
		
		String regularFieldValue = (String)searchHitSource.get(fieldName);
		// default value to regular field value
		String fieldValue = regularFieldValue;
		HighlightField highlightField = highlightFields.get(fieldName);
		// folded term means it has the "folded" filter applied which combines case-insensitive & diacritic insensitive filters
		HighlightField foldedHighlightField = highlightFields.get(fieldName + ".folded");

		if (highlightField != null) {
			fieldValue = (String) highlightField.getFragments()[0].string();
		} else if(foldedHighlightField != null) {
			fieldValue = foldedHighlightField.getFragments()[0].string();			
		}
		
		if(fieldValue != null && !fieldValue.equals(regularFieldValue)) {
			fieldValue = addEllipsesToHighlightedFieldValue(searchHitSource, fieldName, fieldValue);
		}
		
		return fieldValue;
	}
		
	public static String findFirstHighlightedField(Map<String, Object> searchHitSource, Map<String, HighlightField> highlightFields, String... fieldNames) {
		String highlightedValue = null;
		
		for(String fieldName : fieldNames) {
			HighlightField highlightField = highlightFields.get(fieldName);
			
			if(highlightField != null) {
				// if it's matching on the folded value, then that's what the highlighted field will contain, the value of the folded field
				highlightedValue = addEllipsesToHighlightedFieldValue(searchHitSource, fieldName, highlightField.getFragments()[0].string());
				break;
			}
		}
		
		return highlightedValue;
	}
	
	public static String addEllipsesToHighlightedFieldValue(Map<String, Object> searchHitSource, String fieldName, String highlightedFieldValue) {
		String strippedHighlightValue = StringUtils.remove(StringUtils.remove(highlightedFieldValue, "<em class=\"highlight\">"), "</em>");		
				
		// just making an assumption here that if the field is 10 characters or less it's not truncated
		if(strippedHighlightValue.length() > 10) {
			// The folded value is a version of a field that applies a diacritic & case-insensitive filter, that field might be the match
			// but won't be part of the item source returned on the search hit. For example, the user may have searched for "uber" but a field contains something
			// like "�ber den Ursprung und", the folded version of this field would be a hit but the source returned will only contain the original
			// field value. We need to compare to the non-folded original version of the field to the highlighted value to see if the highlighted value is
			// a truncated version of the original and add ellipses to the beginning and/or end 
			fieldName = fieldName.endsWith(".folded") ? fieldName.substring(0, fieldName.lastIndexOf('.')) : fieldName; 
			
			// not truncating tag values at the moment
			if(!"tags".equals(fieldName)) {
				String matchedFieldValue = (String)searchHitSource.get(fieldName);	
				if(StringUtils.isNotEmpty(matchedFieldValue)) {
					// if the first 10 characters match, we're assuming the start of the field wasn't truncated
					if(!strippedHighlightValue.startsWith(matchedFieldValue.substring(0, 10))) {
						highlightedFieldValue = "..." + highlightedFieldValue;
					}
					
					// if the last 10 characters match, we're assuming the end of the field wasn't truncated
					if(!strippedHighlightValue.endsWith(matchedFieldValue.substring(matchedFieldValue.length() - 10))) {
						highlightedFieldValue = highlightedFieldValue + "...";
					}
				}
			}			
		}
		
		return highlightedFieldValue;
	}	
	
	public static Map<String, String> getHighlightFieldValues(Map<String, Object> searchHitSource, Map<String, HighlightField> highlightFields) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		
		if(MapUtils.isNotEmpty(highlightFields)) {
			for(String fieldName : highlightFields.keySet()) {
				// different indexed variations of the same field have a dot, (title, title.folded, title.zero_trim, etc.) 
				// we want to roll them up into one search hit even if more than one of these variations was a match
				int indexOfPeriod = fieldName.indexOf('.');
				String strippedFieldName = indexOfPeriod > 0 ? fieldName.substring(0, indexOfPeriod) : fieldName;
				
				// hack to keep authors from ever being shown in highlighted search hit, it's a special field that we search but suppress from
				// search hits because authority list version of an author name can be offensive to some people, e.g. if the name has the word "saint" and
				// some religions don't recognize the person as a saint
				if(!"authors".equals(strippedFieldName)) {
					String value = (String)highlightFields.get(fieldName).getFragments()[0].string();
					value = addEllipsesToHighlightedFieldValue(searchHitSource, fieldName, value);

					
					// if we don't have a match for this field yet, or this variation has more highlights, add the match
					if(!fieldValues.containsKey(strippedFieldName) || StringUtils.countMatches(value, "<em class=\"highlight\"") > StringUtils.countMatches(fieldValues.get(strippedFieldName), "<em class=\"highlight\"")) {					
						fieldValues.put(strippedFieldName, value);
					}
				}				
			}
		}
		
		return fieldValues;
	}
}
