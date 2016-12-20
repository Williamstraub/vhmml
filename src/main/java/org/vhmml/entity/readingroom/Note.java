package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reading_room_notes")
public class Note {

	public enum Type {
		CONDITION,
		ACKNOWLEDGMENT,
		OTHER
	};
	
	@Id
	@GeneratedValue
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
