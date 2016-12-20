package org.vhmml.dto;

import java.util.List;

import org.vhmml.entity.readingroom.Import;

public class ImportResponse {

	private String message;
	private List<Import> imports;

	public ImportResponse() {
		super();
	}
	
	public ImportResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Import> getImports() {
		return imports;
	}

	public void setImports(List<Import> imports) {
		this.imports = imports;
	}
}
