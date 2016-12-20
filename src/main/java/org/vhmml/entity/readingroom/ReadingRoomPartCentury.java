package org.vhmml.entity.readingroom;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_part_centuries")
public class ReadingRoomPartCentury implements Identifiable {	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "part_id")
    private ReadingRoomObjectPart parentPart;

    private Integer century;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ReadingRoomObjectPart getParentPart() {
		return parentPart;
	}

	public void setParentPart(ReadingRoomObjectPart parentPart) {
		this.parentPart = parentPart;
	}

	public Integer getCentury() {
		return century;
	}

	public void setCentury(Integer century) {
		this.century = century;
	}
}
