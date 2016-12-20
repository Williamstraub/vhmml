package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.repository.BatchSaveRepository;

@Repository
public interface ReadingRoomContentRepository extends CrudRepository<Content, Long>, BatchSaveRepository<Content> {

	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_content where reading_room_object_part_id in(select p.id from reading_room_object_parts p join reading_room_objects o on p.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
