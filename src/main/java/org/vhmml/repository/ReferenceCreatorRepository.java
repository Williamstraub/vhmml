package org.vhmml.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.ReferenceCreator;

@Repository
public interface ReferenceCreatorRepository extends CrudRepository<ReferenceCreator, Long> {
	
}
