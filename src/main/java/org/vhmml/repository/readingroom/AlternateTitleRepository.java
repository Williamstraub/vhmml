package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.AlternateTitle;

@Repository
public interface AlternateTitleRepository extends CrudRepository<AlternateTitle, Long> {
	
public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "DELETE FROM reading_room_alternate_titles WHERE content_id IN (SELECT c.id FROM reading_room_content c JOIN reading_room_object_parts p ON c.reading_room_object_part_id = p.id JOIN reading_room_objects o ON p.reading_room_object_id = o.id WHERE o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
