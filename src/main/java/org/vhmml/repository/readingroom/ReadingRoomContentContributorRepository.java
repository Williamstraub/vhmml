package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.repository.BatchSaveRepository;

@Repository
public interface ReadingRoomContentContributorRepository extends CrudRepository<ContentContributor, Long>, BatchSaveRepository<ContentContributor> {

	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_content_contributors where reading_room_content_id in(select c.id from reading_room_content c join reading_room_object_parts p on c.reading_room_object_part_id = p.id join reading_room_objects o on p.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
