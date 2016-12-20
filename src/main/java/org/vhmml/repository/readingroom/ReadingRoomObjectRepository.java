package org.vhmml.repository.readingroom;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.repository.BatchSaveRepository;

/**
 * This interface extends both CrudRepository and BatchSaveRepository so that when we autowire
 * this interface into an object, Spring will inject a proxy that has the basic
 * CRUD methods from {@link JpaRepository} and also the methods from our custom {@link ReadingRoomObjectRepositoryImpl}
 * class, which provides a bulk insert that does a real batch insert (as opposed to JPARepository's save(Collection) method
 * which simply iterates over the list calling insert for each record).
 */
@Repository
public interface ReadingRoomObjectRepository extends CrudRepository<ReadingRoomObject, Long>, BatchSaveRepository<ReadingRoomObject> {
	
	static final String SEARCH_QUERY = "select t from LexiconTerm t where t.removed = 0 and (upper(t.term) like %?1% or upper(t.shortDefinition) like %?1% or upper(t.fullDefinition) like %?1%) order by t.term";
	
	static final String UPDATE_OBJECT_TYPE_QUERY = "update ReadingRoomObject set objectType = ?1 where id = ?2";
	static final String DELETE_BY_IMPORT_SOURCE_QUERY = "delete from reading_room_objects where data_source = ?1";	
	static final String SELECT_OBJECT_ID_BY_DATASOURCE = "select id from reading_room_objects where data_source = ?1";	
	static final String SELECT_LOCKED_QUERY = "select o from ReadingRoomObject o where locked = 1";
	static final String SELECT_LOCKED_BY_USERNAME_QUERY = "select o from ReadingRoomObject o where locked = 1 and lockedBy = ?1";
	
	static final String SELECT_BY_FEATURE_QUERY = "select o from ReadingRoomObject o join o.features f where f.id = ?1";
	static final String SELECT_BY_GENRE_QUERY = "select o from ReadingRoomObject o join o.genres g where g.id = ?1";
	static final String SELECT_BY_FORMAT_QUERY = "select o from ReadingRoomObject o join o.surrogateFormat f where f.id = ?1";
	
	static final String SELECT_BY_ARCHIVAL_CONTAINER_QUERY = 
		"select " + 
			"o.* " +     
		"from " +
			"reading_room_objects o join " + 
		    "reading_room_archival_data ad on o.id = ad.reading_room_object_id join " +
		    "reading_room_archival_containers ac on ad.container_type = ac.name " +
		"where ac.id = ?1";
	
	static final String SELECT_BY_LANGUAGE_QUERY = 
			"-- archival languages at the object level\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_archival_data ad on o.id = ad.reading_room_object_id join\r\n" + 
			"   reading_room_archival_data_languages adl on ad.id = adl.archival_data_id join\r\n" + 
			"   reading_room_languages l on adl.language_id = l.id\r\n" + 
			"where l.id = ?1\r\n" + 
			"union\r\n" + 
			"-- archival languages at content level\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_archival_data ad on o.id = ad.reading_room_object_id join\r\n" + 
			"   reading_room_archival_content ac on ad.id = ac.archival_data_id join\r\n" + 
			"   reading_room_content c on ac.id = c.id join\r\n" + 
			"   reading_room_content_languages cl on cl.reading_room_content_id = c.id join\r\n" + 
			"   reading_room_languages l on l.id = cl.language_id\r\n" + 
			"where l.id = ?1\r\n" + 
			"-- manuscript/print content languages\r\n" + 
			"union\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"    reading_room_object_parts p on o.id = p.reading_room_object_id join    \r\n" + 
			"    reading_room_content c on p.id = c.reading_room_object_part_id join\r\n" + 
			"    reading_room_content_languages cl on cl.reading_room_content_id = c.id join\r\n" + 
			"    reading_room_languages l on l.id = cl.language_id\r\n" + 
			"where l.id = ?1";
	
	static final String SELECT_BY_UNIFORM_TITLE_QUERY = 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"    reading_room_object_parts p on o.id = p.reading_room_object_id join\r\n" + 
			"    reading_room_content c on p.id = c.reading_room_object_part_id join\r\n" + 
			"    reading_room_uniform_titles ut on c.uniform_title_id = ut.id\r\n" + 
			"where ut.id = ?1";
	
	static final String SELECT_BY_CONTRIBUTOR_QUERY = "-- object contributors\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_object_contributors oc on o.id = oc.reading_room_object_id join\r\n" + 
			"   reading_room_contributors c on oc.contributor_id = c.id\r\n" + 
			"where c.id = ?1\r\n" + 
			"union\r\n" + 
			"-- part contributors\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_object_parts p on o.id = p.reading_room_object_id join\r\n" + 
			"   reading_room_part_contributors pc on p.id = pc.part_id join\r\n" + 
			"   reading_room_contributors c on pc.contributor_id = c.id\r\n" + 
			"where c.id = ?1\r\n" + 
			"union\r\n" + 
			"-- content contributors    \r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_object_parts p on o.id = p.reading_room_object_id join\r\n" + 
			"   reading_room_content content on p.id = content.reading_room_object_part_id join\r\n" + 
			"   reading_room_content_contributors cc on content.id = cc.reading_room_content_id join\r\n" + 
			"   reading_room_contributors c on cc.reading_room_contributor_id = c.id\r\n" + 
			"where c.id = ?1\r\n" + 
			"union\r\n" + 
			"-- archival content contributors    \r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_archival_data ad on o.id = ad.reading_room_object_id join\r\n" + 
			"   reading_room_archival_content ac on ad.id = ac.archival_data_id join\r\n" + 
			"   reading_room_content content on ac.id = content.id join\r\n" + 
			"   reading_room_content_contributors cc on content.id = cc.reading_room_content_id join\r\n" + 
			"   reading_room_contributors c on cc.reading_room_contributor_id = c.id\r\n" + 
			"where c.id = ?1\r\n" + 
			"union\r\n" + 
			"-- archival data contributors\r\n" + 
			"select \r\n" + 
			"	o.*\r\n" + 
			"from \r\n" + 
			"	reading_room_objects o join \r\n" + 
			"   reading_room_archival_data ad on o.id = ad.reading_room_object_id join\r\n" + 
			"   reading_room_archival_contributors ac on ad.id = ac.archival_data_id join\r\n" + 
			"   reading_room_contributors c on ac.contributor_id = c.id\r\n" + 
			"where c.id = ?1";

	@Modifying
	@Query(value = DELETE_BY_IMPORT_SOURCE_QUERY, nativeQuery = true)
	public void deleteByImportSource(String importSource);
	
	@Modifying
	@Query(value = UPDATE_OBJECT_TYPE_QUERY)
	public void updateObjectType(ReadingRoomObject.Type type, Long id);
	
	@Query(value = SELECT_OBJECT_ID_BY_DATASOURCE, nativeQuery = true)
	public List<Integer> selectObjectIdsByDataSource(String dataSource);
	
	public ReadingRoomObject findByHmmlProjectNumber(String hmmlProjectNumber);
	
	public List<ReadingRoomObject> findByHmmlProjectNumberStartsWith(String collectionName);
	public List<ReadingRoomObject> findByCountryId(Long countryId);
	public List<ReadingRoomObject> findByCityId(Long cityId);
	public List<ReadingRoomObject> findByRepositoryId(Long repositoryId);
	public List<ReadingRoomObject> findByHoldingInstitutionId(Long holdingInstituionId);
	
	@Query(SELECT_BY_FEATURE_QUERY)
	public List<ReadingRoomObject> findByFeatureId(Long featureId);
	
	@Query(SELECT_BY_GENRE_QUERY)
	public List<ReadingRoomObject> findByGenreId(Long genreId);
	
	@Query(SELECT_BY_FORMAT_QUERY)
	public List<ReadingRoomObject> findByFormatId(Long formatId);
	
	@Query(value = SELECT_BY_LANGUAGE_QUERY, nativeQuery = true)
	public List<ReadingRoomObject>findByLanguageId(Long languageId);
	
	@Query(value = SELECT_BY_ARCHIVAL_CONTAINER_QUERY, nativeQuery = true)
	public List<ReadingRoomObject> findByArchivalContainerId(Long containerId);
	
	@Query(value = SELECT_BY_UNIFORM_TITLE_QUERY, nativeQuery = true)
	public List<ReadingRoomObject> findByUniformTitleId(Long uniformTitleId);
	
	@Query(value = SELECT_BY_CONTRIBUTOR_QUERY, nativeQuery = true)
	public List<ReadingRoomObject> findByContributorId(Long contributorId);
	
	@Query(SELECT_LOCKED_QUERY)
	@Lock(LockModeType.PESSIMISTIC_READ)
	public List<ReadingRoomObject> findLocked();
	
	@Query(SELECT_LOCKED_BY_USERNAME_QUERY)
	@Lock(LockModeType.PESSIMISTIC_READ)
	public List<ReadingRoomObject> findLockedByUsername(String username);
}
