package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reading_room_object_alternate_surrogates")
public class AlternateSurrogate {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "reading_room_object_id")
	private Long readingRoomObjectId;
	
	private String name;

	public AlternateSurrogate() {
		super();
	}
	
	public AlternateSurrogate(String name, Long readingRoomObjectId) {
		this.readingRoomObjectId = readingRoomObjectId;
		this.name = name;
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReadingRoomObjectId() {
		return readingRoomObjectId;
	}

	public void setReadingRoomObjectId(Long readingRoomObjectId) {
		this.readingRoomObjectId = readingRoomObjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
