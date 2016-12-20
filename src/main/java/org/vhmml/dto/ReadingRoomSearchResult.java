package org.vhmml.dto;

import java.util.List;

public class ReadingRoomSearchResult extends SearchResult {

	private List<ReadingRoomSearchHit> searchHits;

	public List<ReadingRoomSearchHit> getSearchHits() {
		return searchHits;
	}

	public void setSearchHits(List<ReadingRoomSearchHit> searchHits) {
		this.searchHits = searchHits;
	}
}
