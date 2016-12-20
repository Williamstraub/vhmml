package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.repository.BatchSaveRepository;

@Repository
public interface ArchivalDataRepository extends CrudRepository<ArchivalData, Long>, BatchSaveRepository<ArchivalData> {

	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_archival_data where reading_room_object_id in(select o.id from reading_room_objects o where o.data_source = ?1)";
	public static final String DELETE_BY_OBJECT_ID_QUERY = "delete from reading_room_archival_data where reading_room_object_id = ?1";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
	
	@Modifying
	@Query(value = DELETE_BY_OBJECT_ID_QUERY, nativeQuery = true)
	public void deleteByReadingRoomObjectId(Long readingRoomObjectId);
}
