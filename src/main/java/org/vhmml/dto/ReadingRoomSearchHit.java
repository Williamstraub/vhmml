package org.vhmml.dto;

import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;

public class ReadingRoomSearchHit extends SearchHit {

	private ReadingRoomObjectOverview objectOverview;	

	public ReadingRoomObjectOverview getObjectOverview() {
		return objectOverview;
	}

	public void setObjectOverview(ReadingRoomObjectOverview objectOverview) {
		this.objectOverview = objectOverview;
	}
}
