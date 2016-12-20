package org.vhmml.dto;

import java.util.List;
import java.util.Map;

import org.vhmml.dto.elasticsearch.IndexedType;

public class Report extends SearchResult {

	public enum Type {
		OBJECT("Object", IndexedType.IMAGE_VIEW),
		USER("User", IndexedType.IMAGE_VIEW),
		POPULARITY("Popularity", IndexedType.IMAGE_VIEW),
		CATALOGER("Cataloger", IndexedType.READING_ROOM_OBJECT_SAVE);
		
		// the index document type to search for the report, e.g. IMAGE_VIEW index holds the data we search for object report
		private String displayName;
		private IndexedType indexedType;
		
		Type(String displayName, IndexedType indexedType) {
			this.displayName = displayName;
			this.indexedType = indexedType;
		}
		
		public IndexedType getIndexedType() {
			return indexedType;
		}
		
		public String getDisplayName() {
			return displayName;
		}
	}

	private String fieldName;
	private List<Map<String, Object>> data;
	
	public Report(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
