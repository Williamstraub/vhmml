package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

@Entity
@Table(name = "reading_room_object_subjects")
public class ReadingRoomObjectSubject implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "reading_room_object_id")
	private Long readingRoomObjectId;
	
	@Column(name = "subject_id")
	private Long subjectId;

	public ReadingRoomObjectSubject() {
		super();
	}
	
	public ReadingRoomObjectSubject(Long readingRoomObjectId, Long subjectId) {
		this.readingRoomObjectId = readingRoomObjectId;
		this.subjectId = subjectId;
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

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}
}
