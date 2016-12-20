package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

@Entity
@Table(name = "reading_room_archival_data_languages")
public class ArchivalDataLanguage implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
    @Column(name = "archival_data_id")
    private Long archivalDataId;

	@Column(name = "language_id")
    private Long languageId;
	
	public ArchivalDataLanguage() {
		super();
	}
	
	public ArchivalDataLanguage(Long archivalDataId, Long languageId) {
		this.archivalDataId = archivalDataId;
		this.languageId = languageId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArchivalDataId() {
		return archivalDataId;
	}

	public void setArchivalDataId(Long archivalDataId) {
		this.archivalDataId = archivalDataId;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
}
