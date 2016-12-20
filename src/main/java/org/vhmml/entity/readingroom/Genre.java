package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.vhmml.entity.NamedIdentifiable;

@Entity
@Table(name = "reading_room_genres")
public class Genre extends NamedIdentifiable {
	
	@Column(name = "uri")
	private String uri;
	
	public Genre() {
		super();
	}
	
	public Genre(Long id) {
		this.id = id;
	}
	
	public Genre(String name) {
		setName(name);
	}
	
	public Genre(Long id, String name, String uri) {
		this(name);
		this.id = id;
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
