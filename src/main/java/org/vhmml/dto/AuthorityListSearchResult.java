package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.vhmml.entity.NamedIdentifiable;

public class AuthorityListSearchResult extends SearchResult {

	private List<? extends NamedIdentifiable> authorityListObjects = new ArrayList<>();
	
	public AuthorityListSearchResult() {
		super();
	}

	public AuthorityListSearchResult(Page<? extends NamedIdentifiable> searchResult) {

		this.authorityListObjects = searchResult.getContent();
		setPageNumber(searchResult.getNumber());
		setPageSize(searchResult.getSize());
		setTotalElements(searchResult.getTotalElements());
	}

	public List<? extends NamedIdentifiable> getAuthorityListObjects() {
		return authorityListObjects;
	}

	public void setAuthorityListObjects(List<? extends NamedIdentifiable> authorityListObjects) {
		this.authorityListObjects = authorityListObjects;
	}

}
