package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_archival_content_centuries")
public class ArchivalContentCentury {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "archival_content_id")
    private ArchivalData parentArchivalContent;

    private Integer century;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArchivalData getParentArchivalContent() {
		return parentArchivalContent;
	}

	public void setParentArchivalContent(ArchivalData parentArchivalContent) {
		this.parentArchivalContent = parentArchivalContent;
	}

	public Integer getCentury() {
		return century;
	}

	public void setCentury(Integer century) {
		this.century = century;
	}
}
