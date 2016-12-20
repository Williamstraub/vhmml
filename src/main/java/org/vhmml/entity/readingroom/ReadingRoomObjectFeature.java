package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.vhmml.entity.Identifiable;

@Entity
@Table(name = "reading_room_object_features")
public class ReadingRoomObjectFeature implements Identifiable {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "reading_room_object_id")
	private Long readingRoomObjectId;
	
	@Column(name = "feature_id")
	private Long featureId;
	
	public ReadingRoomObjectFeature() {
		super();
	}
	
	public ReadingRoomObjectFeature(Long featureId) {
		this.featureId = featureId; 
	}
	
	public ReadingRoomObjectFeature(Long readingRoomObjectId, Long featureId) {
		this.readingRoomObjectId = readingRoomObjectId;
		this.featureId = featureId; 
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

	public Long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
}
