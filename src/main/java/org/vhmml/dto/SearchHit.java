package org.vhmml.dto;

import java.util.Map;

public class SearchHit {
	private Map<String, String> highlightFields;
	private boolean editable;
	
	public Map<String, String> getHighlightFields() {
		return highlightFields;
	}

	public void setHighlightFields(Map<String, String> highlightFields) {
		this.highlightFields = highlightFields;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
