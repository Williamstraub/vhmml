package org.vhmml.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

static final String SELECT_ALL_QUERY = "select t from Tag t";
	
	@Query(SELECT_ALL_QUERY)
	@Lock(LockModeType.PESSIMISTIC_READ)
	List<Tag> findAllForUpdate();
}
