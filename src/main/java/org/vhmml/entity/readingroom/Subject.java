package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.AuthorityControl;

@Entity
@Table(name = "reading_room_subjects")
public class Subject extends AuthorityControl {		
	
	public Subject() {
		super();
	}
	
	public Subject(String name) {
		this.name = name;
	}
	
	public Subject(String name, String authorityUriLC, String authorityUriVIAF) {
		this(name);
		this.setAuthorityUriLC(authorityUriLC);
		this.setAuthorityUriVIAF(authorityUriVIAF);
	}
}
