package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.repository.BatchSaveRepository;

@Repository
public interface ReadingRoomAlternateSurrogateRepository extends CrudRepository<AlternateSurrogate, Long>, BatchSaveRepository<AlternateSurrogate> {

	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_object_alternate_surrogates where reading_room_object_id in(select o.id from reading_room_objects o where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
	
	public void deleteByReadingRoomObjectId(Long objectId);
}
