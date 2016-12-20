package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.readingroom.ArchivalContributor;
import org.vhmml.entity.readingroom.ArchivalDataCentury;
import org.vhmml.entity.readingroom.ArchivalDataLanguage;
import org.vhmml.repository.BatchSaveRepository;

public interface ArchivalDataLanguageRepository extends CrudRepository<ArchivalDataLanguage, Long>, BatchSaveRepository<ArchivalDataLanguage> {
public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_archival_data_languages where archival_data_id in(select ad.id from reading_room_archival_data ad join reading_room_objects o on ad.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
