package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_scripts")
public class Script extends NamedIdentifiable {
	
	public Script() {
		super();
	}
	
	public Script(Long id) {
		this.id = id;
	}
	
	public Script(String name) {
		setName(name);
	}
}
