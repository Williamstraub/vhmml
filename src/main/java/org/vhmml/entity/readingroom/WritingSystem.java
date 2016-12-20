package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_writing_systems")
public class WritingSystem extends NamedIdentifiable {
	
	public WritingSystem() {
		super();
	}
	
	public WritingSystem(Long id) {
		this.id = id;
	}
	
	public WritingSystem(String name) {
		setName(name);
	}
}
