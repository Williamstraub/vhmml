package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.AuthorityControl;

@Entity
@Table(name = "reading_room_uniform_titles")
public class UniformTitle extends AuthorityControl {	

	public UniformTitle() {
		super();
	}
	
	public UniformTitle(String name, String authorityUriLC, String authorityUriVIAF) {
		super(name, authorityUriLC, authorityUriVIAF);
	}
	
	public UniformTitle(Long id, String name, String authorityUriLC, String authorityUriVIAF) {
		this(name, authorityUriLC, authorityUriVIAF);
		this.id = id;
	}
}