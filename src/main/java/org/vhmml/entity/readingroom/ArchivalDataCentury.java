package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_archival_data_centuries")
public class ArchivalDataCentury {

	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "archival_data_id")
    private ArchivalData parentArchivalData;

    private Integer century;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArchivalData getParentArchivalData() {
		return parentArchivalData;
	}

	public void setParentArchivalData(ArchivalData parentArchivalData) {
		this.parentArchivalData = parentArchivalData;
	}

	public Integer getCentury() {
		return century;
	}

	public void setCentury(Integer century) {
		this.century = century;
	}
}
