package org.vhmml.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.LexiconContributor;

@Repository
public interface LexiconContributorRepository extends CrudRepository<LexiconContributor, Long> {
	
	public void deleteByLexiconId(Long lexiconId);
}
