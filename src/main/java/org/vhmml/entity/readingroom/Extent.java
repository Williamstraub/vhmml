package org.vhmml.entity.readingroom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reading_room_object_extents")
public class Extent {
	
	@Id
	@GeneratedValue
	private Long id;	
	
	@Column(name = "reading_room_object_id")
	private Long readingRoomObjectId;
	
	@Column(name = "extent_count")
	private Integer count;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "extent_type")
	private ReadingRoomObject.ExtentType type;
	
	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "folio_imported")
	private String folioImported;

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

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public ReadingRoomObject.ExtentType getType() {
		return type;
	}

	public void setType(ReadingRoomObject.ExtentType type) {
		this.type = type;
		
		if(type != null) {
			this.displayName = type.getDisplayName();
		}
	}

	public String getFolioImported() {
		return folioImported;
	}

	public void setFolioImported(String folioImported) {
		this.folioImported = folioImported;
	}

	public String getDisplayName() {
		
		if(this.type != null) {
			this.displayName = this.type.getDisplayName();
		}
		
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
