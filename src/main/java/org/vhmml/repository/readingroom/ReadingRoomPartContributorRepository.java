package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.repository.BatchSaveRepository;

public interface ReadingRoomPartContributorRepository extends CrudRepository<ReadingRoomPartContributor, Long>, BatchSaveRepository<ReadingRoomPartContributor> {
	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_part_contributors where part_id in(select p.id from reading_room_object_parts p join reading_room_objects o on p.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
