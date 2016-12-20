package org.vhmml.entity.readingroom;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.apache.commons.collections4.CollectionUtils;

@Entity
@DiscriminatorValue(value = "ARCHIVAL_OBJECT")
public class ArchivalObject extends ReadingRoomObject {
	
	// manuscript & print objects are almost identical, so we have a 1 table/class
	// inheritance strategy, however archival documents are very different so 
	// we don't want to pollute the reading_room_object table with a bunch of archival 
	// fields.  Therefore, we have an archival data table that holds the attributes
	// specific to archival items. This also allows us to eliminate the need for 
	// archival items to follow the object->part->content hierarchy of manuscript & print
	// objects, archival objects really don't have parts, they just have items so we
	// have an archival data -> archival content hierarchy for archival items
	
	public ArchivalObject() {
		super();
	}
	
	public ArchivalObject(Long id) {
		setId(id);
	}
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentObject")	
	private ArchivalData archivalData;

	public ArchivalData getArchivalData() {
		return archivalData;
	}

	public void setArchivalData(ArchivalData archivalData) {
		this.archivalData = archivalData;
		
		if(archivalData != null) {	
			archivalData.setParentObject(this);
			
			List<ArchivalContent> contents = archivalData.getContent();
			
			if(CollectionUtils.isNotEmpty(contents)) {
				for(ArchivalContent content : contents) {
					content.setParentArchivalData(archivalData);
				}
			}
		}		
	}
}
