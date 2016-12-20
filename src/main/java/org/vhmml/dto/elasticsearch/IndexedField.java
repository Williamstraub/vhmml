package org.vhmml.dto.elasticsearch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

public class IndexedField {

	private String name;
	private String type;
	private String analyzer;
	// date format for dates
	private String format;
	// this a map of other variations of the field that are indexed and the corresponding analyzer used to index them
	// for example, we store a zero stripped version of hmml project number so searching for stef 7 returns stef 00007
	// by default, for all fields we store a raw variation for sorting purposes and a "folded" version that is lowercase
	// with diacritics removed
	private Map<String, String> fieldVariants = new HashMap<>();
	
	public IndexedField(String name) {	
		// 8/22/2016 - changed default analyzer to stemming analyzer since we want most fields stemmed to match plural, etc.
		// now we only use the main_analyzer for fields with boolean values so values like "false" don't get stemmed to "fals" 
		this(name, "string", "stemming_analyzer", null);		
		this.fieldVariants = getDefaultFieldVariants();		
	}
	
	public IndexedField(String name, String type, String format) {	 
		this(name, type, "stemming_analyzer", null);
		this.format = format;
		this.fieldVariants = getDefaultFieldVariants();	
	}
	
	public IndexedField(String name, String analyzer, Map<String, String> fieldVariants) {
		this(name, "string", analyzer, fieldVariants);		
	}
	
	public IndexedField(String name, String type, String analyzer, Map<String, String> fieldVariants) {
		this.name = name;
		this.type = type;
		this.analyzer = analyzer;
		this.fieldVariants = getDefaultFieldVariants();
		
		if(MapUtils.isNotEmpty(fieldVariants)) {
			this.fieldVariants.putAll(fieldVariants);			
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(String analyzer) {
		this.analyzer = analyzer;
	}

	public Map<String, String> getFieldVariants() {
		return fieldVariants;
	}

	public void setFieldVariants(Map<String, String> fieldVariants) {
		this.fieldVariants = fieldVariants;
	}
	
	public Map<String, String> getDefaultFieldVariants() {
		Map<String, String> defaultVariations = new HashMap<String, String>();
		// we index a "folded" version of the field that is lowercase & removes diacritics
		defaultVariations.put("folded", "stemming_folding_analyzer");
		// we index a "raw" version of the field that is not broken into tokens and removes quotes for sorting
		defaultVariations.put("raw", "sorting_analyzer");
		defaultVariations.put("basic", "basic_analyzer");
		defaultVariations.put("untouched", "not_analyzed");
		
		return defaultVariations;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
