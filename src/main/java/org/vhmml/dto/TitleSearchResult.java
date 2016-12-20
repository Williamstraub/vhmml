package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.vhmml.entity.readingroom.ContentTitleView;

public class TitleSearchResult extends SearchResult {

	private List<ContentTitleView> titles = new ArrayList<>();
	
	public TitleSearchResult() {
		super();
	}
	
	public TitleSearchResult(Page<ContentTitleView> searchResult) {
		this.titles = searchResult.getContent();
		setPageNumber(searchResult.getNumber());
		setPageSize(searchResult.getSize());
		setTotalElements(searchResult.getTotalElements());
	}

	public List<ContentTitleView> getTitles() {
		return titles;
	}

	public void setTitles(List<ContentTitleView> titles) {
		this.titles = titles;
	}
}
