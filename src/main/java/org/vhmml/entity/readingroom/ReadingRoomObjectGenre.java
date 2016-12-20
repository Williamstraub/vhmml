package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

@Entity
@Table(name = "reading_room_object_genres")
public class ReadingRoomObjectGenre implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "reading_room_object_id")
	private Long readingRoomObjectId;
	
	@Column(name = "genre_id")
	private Long genreId;

	public ReadingRoomObjectGenre() {
		super();
	}
	
	public ReadingRoomObjectGenre(Long readingRoomObjectId, Long genreId) {
		this.readingRoomObjectId = readingRoomObjectId;
		this.genreId = genreId;
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

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}
}
