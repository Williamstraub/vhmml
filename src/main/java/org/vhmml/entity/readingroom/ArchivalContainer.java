package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_archival_containers")
public class ArchivalContainer extends NamedIdentifiable {
	
	public ArchivalContainer() {
		super();
	}
	
	public ArchivalContainer(String name) {
		setName(name);
	}
	
	public ArchivalContainer(Long id, String name) {
		setName(name);
		this.id = id;
	}
}
