package org.vhmml.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.vhmml.dto.LexiconSearchResult;
import org.vhmml.dto.LexiconTermView;
import org.vhmml.entity.Contributor;
import org.vhmml.entity.LexiconTerm;

public interface LexiconService {
	
	public LexiconTerm findById(Long id);
	public List<LexiconTermView> findAll();
	public LexiconSearchResult search(String searchText, Pageable pageable);
	public LexiconSearchResult termContainsSearch(String term);
	public LexiconSearchResult startsWithSearch(String term, Pageable pageable);
	public LexiconTerm save(LexiconTerm term);
	public List<Contributor> findContributorsByName(String name);
	public void delete(Long id);
	public LexiconTerm remove(Long id);
}
