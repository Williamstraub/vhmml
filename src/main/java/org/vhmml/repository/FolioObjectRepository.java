package org.vhmml.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.FolioObject;

@Repository
public interface FolioObjectRepository  extends CrudRepository<FolioObject, Long> {

	public FolioObject findByFolioObjectNumber(String folioObjectNumber);
	
	@Query(value = "select distinct language from folio_objects where language is not null and language <> '' order by language", nativeQuery = true)
	public List<String> findLanguages();
	
	@Query(value = "select distinct writing_system from folio_objects where writing_system is not null and writing_system <> '' order by writing_system", nativeQuery = true)
	public List<String> findWritingSystems();
	
	@Query(value = "select distinct script from folio_objects where script is not null and script <> '' order by script", nativeQuery = true)
	public List<String> findScripts();
}