package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ArchivalContent;

@Repository
public interface ArchivalContentRepository extends CrudRepository<ArchivalContent, Long> {

	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_archival_content where archival_data_id in(select ad.id from reading_room_archival_data ad join reading_room_objects o on ad.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
