package org.vhmml.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.ReferenceEntry;

@Repository
public interface ReferenceEntryRepository extends CrudRepository<ReferenceEntry, Long> {
	
	static final String SELECT_ALL_QUERY = "select le from ReferenceEntry le";
	
	@Query(SELECT_ALL_QUERY)
	@Lock(LockModeType.PESSIMISTIC_READ)
	public List<ReferenceEntry> findAllForUpdate();
	
	ReferenceEntry findByZoteroKey(String zoteroKey);
	
	public Integer countByZoteroGroupIdAndZoteroCollectionKey(Integer zoteroGroupId, String zoteroCollectionKey);
	public void deleteByZoteroGroupIdAndZoteroCollectionKey(Integer zoteroGroupId, String zoteroCollectionKey);
}
