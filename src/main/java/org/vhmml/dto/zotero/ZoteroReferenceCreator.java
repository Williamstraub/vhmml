package org.vhmml.dto.zotero;

import org.vhmml.util.ZoteroReferenceCreatorDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ZoteroReferenceCreatorDeserializer.class)
public class ZoteroReferenceCreator {

	private String creatorType;
	private String name;
	private String firstName;
	private String lastName;
	
	public ZoteroReferenceCreator() {
		super();
	}

	public String getCreatorType() {
		return creatorType;
	}

	public void setCreatorType(String creatorType) {
		this.creatorType = creatorType;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
