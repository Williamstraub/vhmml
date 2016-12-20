package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryRepository extends NamedIdentifiableRepository<org.vhmml.entity.readingroom.Repository> {
	
	public List<org.vhmml.entity.readingroom.Repository> findAllByOrderByNameAsc();
	public org.vhmml.entity.readingroom.Repository findByName(String name);
	public org.vhmml.entity.readingroom.Repository findByNameAndCityId(String name, Long cityId);	
	public List<org.vhmml.entity.readingroom.Repository> findByCityIdOrderByNameAsc(Long cityId);
}
