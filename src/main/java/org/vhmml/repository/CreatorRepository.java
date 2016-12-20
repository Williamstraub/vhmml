package org.vhmml.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.Creator;

@Repository
public interface CreatorRepository extends CrudRepository<Creator, Long> {

	static final String SELECT_ALL_QUERY = "select c from Creator c";
	
	@Query(SELECT_ALL_QUERY)
	@Lock(LockModeType.PESSIMISTIC_READ)
	List<Creator> findAllForUpdate();
}
