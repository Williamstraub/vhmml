package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.vhmml.entity.readingroom.ReadingRoomPartCentury;

public interface ReadingRoomPartCenturyRepository extends CrudRepository<ReadingRoomPartCentury, Long> {
	
	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_part_centuries where part_id in(select p.id from reading_room_object_parts p join reading_room_objects o on p.reading_room_object_id = o.id where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
}
