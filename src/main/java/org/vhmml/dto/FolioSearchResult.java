package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

public class FolioSearchResult extends SearchResult {

	private List<FolioSearchHit> searchHits = new ArrayList<>();

	public List<FolioSearchHit> getSearchHits() {
		return searchHits;
	}

	public void setSearchHits(List<FolioSearchHit> searchHits) {
		this.searchHits = searchHits;
	}
}
