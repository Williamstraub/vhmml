package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_features")
public class Feature extends NamedIdentifiable {	

	public Feature() {
		super();
	}
	
	public Feature(Long id) {
		this.id = id;
	}
	
	public Feature(String name) {
		setName(name);
	}
	
	public Feature(Long id, String name) {
		this(name);
		this.id = id;
	}
}
