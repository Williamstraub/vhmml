package org.vhmml.exception;

import java.util.List;

import org.vhmml.entity.readingroom.ReadingRoomObject;

public class ExistingReferencesException extends Exception {	

	private static final long serialVersionUID = -4204518000429559305L;

	private List<ReadingRoomObject> references;
	
	public ExistingReferencesException(String message, List<ReadingRoomObject> references) {
		super(message);
		this.references = references;
	}
	
	public List<ReadingRoomObject> getReferences() {
		return references;
	}
}
