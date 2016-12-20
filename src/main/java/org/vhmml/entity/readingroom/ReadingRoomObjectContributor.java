package org.vhmml.entity.readingroom;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.vhmml.entity.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reading_room_object_contributors")
public class ReadingRoomObjectContributor implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private Contributor.Type type;
	
	// name native script is stored specific to the object because the same contributor might have Arabic native 
	// script name on one object and some other language on another object
	@Column(name = "name_ns")
	private String nameNs;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name = "reading_room_object_id")
    private ReadingRoomObject readingRoomObject;

	@OrderBy("name")
	@Fetch(FetchMode.JOIN)
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contributor_id")
    private Contributor contributor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Contributor.Type getType() {
		return type;
	}

	public void setType(Contributor.Type type) {
		this.type = type;
	}

	public ReadingRoomObject getReadingRoomObject() {
		return readingRoomObject;
	}

	public void setReadingRoomObject(ReadingRoomObject readingRoomObject) {
		this.readingRoomObject = readingRoomObject;
	}

	public Contributor getContributor() {
		return contributor;
	}

	public void setContributor(Contributor contributor) {
		this.contributor = contributor;
	}

	public String getNameNs() {
		return nameNs;
	}

	public void setNameNs(String nameNs) {
		this.nameNs = nameNs;
	}
}
