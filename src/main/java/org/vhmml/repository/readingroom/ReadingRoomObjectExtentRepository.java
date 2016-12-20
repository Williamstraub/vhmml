package org.vhmml.repository.readingroom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.repository.BatchSaveRepository;

/**
 * This interface extends both CrudRepository and BatchSaveRepository so that when we autowire
 * this interface into an object, Spring will inject a proxy that has the basic
 * CRUD methods from {@link JpaRepository} and also the methods from our custom {@link ReadingRoomObjectExtentRepositoryImpl}
 * class, which provides a bulk insert that does a real batch insert (as opposed to JPARepository's save(Collection) method
 * which simply iterates over the list calling insert for each record).
 */
@Repository
public interface ReadingRoomObjectExtentRepository extends CrudRepository<Extent, Long>, BatchSaveRepository<Extent> {
	
	public static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_object_extents where reading_room_object_id in(select o.id from reading_room_objects o where o.data_source = ?1)";
	
	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
	
	public void deleteByReadingRoomObjectId(Long objectId);
}
