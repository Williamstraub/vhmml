package org.vhmml.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.LexiconRelatedTerm;

@Repository
public interface LexiconRelatedTermRepository extends CrudRepository<LexiconRelatedTerm, Long> {

	public List<LexiconRelatedTerm> deleteByTermIdAndRelatedTermId(Long termId, Long relatedTermId);
	public List<LexiconRelatedTerm> deleteByTermId(Long termId);
	public List<LexiconRelatedTerm> deleteByRelatedTermId(Long relatedTermId);
}
