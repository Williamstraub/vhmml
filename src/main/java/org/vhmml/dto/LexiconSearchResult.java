package org.vhmml.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.vhmml.entity.LexiconTerm;

public class LexiconSearchResult extends SearchResult {

	private List<LexiconTermView> terms = new ArrayList<LexiconTermView>();

	public LexiconSearchResult() {
		super();
	}
	
	public LexiconSearchResult(List<LexiconTerm> terms) {
		for(LexiconTerm term : terms) {
			this.terms.add(new LexiconTermView(term));
		}
		
		setTotalElements(new Long(this.terms.size()));
	}	
	
	public LexiconSearchResult(Page<LexiconTerm> searchResult) {
		
		for(LexiconTerm term : searchResult.getContent()) {
			this.terms.add(new LexiconTermView(term));
		}
		
		setPageNumber(searchResult.getNumber());
		setPageSize(searchResult.getSize());
		setTotalElements(searchResult.getTotalElements());
	}

	public List<LexiconTermView> getTerms() {
		return terms;
	}

	public void setTerms(List<LexiconTermView> terms) {
		this.terms = terms;
	}
}
