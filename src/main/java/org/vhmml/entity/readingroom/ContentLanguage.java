package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

@Entity
@Table(name = "reading_room_content_languages")
public class ContentLanguage implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "reading_room_content_id")
	private Long readingRoomContentId;
	
	@Column(name = "language_id")
	private Long languageId;	

	public ContentLanguage() {
		super();
	}
	
	public ContentLanguage(Long readingRoomContentId, Long languageId) {
		this.readingRoomContentId = readingRoomContentId;
		this.languageId = languageId;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReadingRoomContentId() {
		return readingRoomContentId;
	}

	public void setReadingRoomContentId(Long readingRoomContentId) {
		this.readingRoomContentId = readingRoomContentId;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
}
