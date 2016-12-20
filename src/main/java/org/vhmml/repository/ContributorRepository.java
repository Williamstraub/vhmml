package org.vhmml.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.Contributor;

@Repository
public interface ContributorRepository extends CrudRepository<Contributor, Long> {

	public Contributor findByName(String name);
	public List<Contributor> findByNameContains(String name);
}
