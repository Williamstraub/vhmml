package org.vhmml.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.ReferenceListItem;

@Repository
public interface ReferenceListRepository extends CrudRepository<ReferenceListItem, Long> {
	
	static final String SELECT_ALL_QUERY = "select li from ReferenceListItem li";
	static final String BY_AUTHOR_QUERY = "select li from ReferenceListItem li left join li.creators c where c.lastName like ?1% or c.firstName like ?1% or c.name like ?1%";
	static final String BY_TITLE_QUERY = "select li from ReferenceListItem li where li.title like %?1% or li.bookTitle like %?1% or li.encyclopediaTitle like %?1%";	

	/* TODO: this is an example of how to do a full text index search if we upgrade MySQL or use MariaDB and decide NOT
	 * to use a search engine like Elastic Search. If we go this route we still need to find a way to make
	 * sorting work correctly since the only way to call a full text query is with a native query and those don't 
	 * support Spring paging.
	 */
	static final String BY_TITLE_NATIVE_QUERY = "SELECT id, " +
			"title, " +
			"book_title, " +
			"series_title, " +
			"encyclopedia_title, " +
			"author, " +
			"publication_title, " +
			"place, " +
			"publisher, " +
			"date, " +
			"series, " +
			"url FROM reference_list " +
		"WHERE " +
			"MATCH(title) AGAINST (?1 IN BOOLEAN MODE) or " +
			"MATCH(book_title) AGAINST (?1 IN BOOLEAN MODE) or " +
			"MATCH(encyclopedia_title) AGAINST (?1 IN BOOLEAN MODE) " +
		"ORDER BY ?2 limit ?3, ?4";
	
	
	static final String BY_KEYWORD_QUERY = "select li from ReferenceListItem li where " + 
			"li.title like %?1% or book_title like %?1% or " +
			"series_title like %?1% or publication_title like %?1% or " +
			"place like %?1% or publisher like %?1% or date like %?1% or " +
			"series like %?1% or url like %?1% or author like %?1%";
	
	@Query(SELECT_ALL_QUERY)
	public Page<ReferenceListItem> findAll(Pageable pageable);
	
	@Query(BY_AUTHOR_QUERY)
	public Page<ReferenceListItem> findByAuthor(String author, Pageable pageable);
	
	@Query(value = BY_TITLE_QUERY)
	public Page<ReferenceListItem> findByTitle(String title, Pageable pageable);
	
	/* TODO: this is an example of how to do a full text index search if we upgrade MySQL or use MariaDB and decide NOT
	 * to use a search engine like Elastic Search. If we go this route we still need to find a way to make
	 * sorting work correctly since the only way to call a full text query is with a native query and those don't 
	 * support Spring paging.
	 */
	@Query(value = BY_TITLE_NATIVE_QUERY, nativeQuery = true)
	public List<ReferenceListItem> findByTitleNative(String title, String orderBy, Integer offset, Integer limit);
	
	@Query(BY_KEYWORD_QUERY)
	public Page<ReferenceListItem> findByKeyword(String keyword, Pageable pageable);
}
