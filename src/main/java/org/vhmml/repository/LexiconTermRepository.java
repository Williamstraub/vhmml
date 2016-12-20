package org.vhmml.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.LexiconTerm;

@Repository
public interface LexiconTermRepository extends CrudRepository<LexiconTerm, Long> {
	
	public LexiconTerm findByTerm(String term);
	
	@Query(SEARCH_QUERY)
	public Page<LexiconTerm> search(String searchText, Pageable pageable);
	public List<LexiconTerm> findByTermStartingWithOrderByTermAsc(String term);
	public List<LexiconTerm> findByTermStartingWithOrderByTermDesc(String term);
	public List<LexiconTerm> findByTermContains(String term);	
	
	static final String SEARCH_QUERY = "select t from LexiconTerm t where t.removed = 0 and (upper(t.term) like %?1% or upper(t.shortDefinition) like %?1% or upper(t.fullDefinition) like %?1%) order by t.term";
}
