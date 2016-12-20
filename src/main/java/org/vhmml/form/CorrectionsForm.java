package org.vhmml.form;

import java.util.List;

public class CorrectionsForm {

	public enum Type {
		REFERENCE("Reference"), 
		LEXICON("Lexicon"),
		READING_ROOM("Reading Room"),
		FOLIO("Folio");
		
		Type(String displayName) {
			this.displayName = displayName;
		}
		
		private String displayName;
		
		public String getDisplayName() {
			return displayName;
		}
	};
	
	private String corrections;
	private boolean allowContact;
	private String contactEmail;
	private List<String> categories;
	private String hmmlProjectNumber;
	private Type type;	
	
	public CorrectionsForm() {
		super();
	}

	public String getCorrections() {
		return corrections;
	}

	public void setCorrections(String corrections) {
		this.corrections = corrections;
	}

	public boolean isAllowContact() {
		return allowContact;
	}

	public void setAllowContact(boolean allowContact) {
		this.allowContact = allowContact;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getHmmlProjectNumber() {
		return hmmlProjectNumber;
	}

	public void setHmmlProjectNumber(String hmmlProjectNumber) {
		this.hmmlProjectNumber = hmmlProjectNumber;
	}
}
