package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.vhmml.entity.ReferenceListItem;

public class ReferenceSearchResult extends SearchResult {

	private List<ReferenceListItem> items = new ArrayList<ReferenceListItem>();
	
	public ReferenceSearchResult() {
		super();
	}	

	public ReferenceSearchResult(Page<ReferenceListItem> searchResult) {

		this.items = searchResult.getContent();
		setPageNumber(searchResult.getNumber());
		setPageSize(searchResult.getSize());
		setTotalElements(searchResult.getTotalElements());
	}

	public List<ReferenceListItem> getItems() {
		return items;
	}

	public void setItems(List<ReferenceListItem> items) {
		this.items = items;
	}
}
