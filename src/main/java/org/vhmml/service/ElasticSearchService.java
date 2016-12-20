package org.vhmml.service;

import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.vhmml.dto.elasticsearch.Aggregation;
import org.vhmml.dto.elasticsearch.DateRangeFilter;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.Identifiable;
import org.vhmml.service.ElasticSearchServiceImpl.Index;

public interface ElasticSearchService {
	public void initializeIndex(Index index);
	public void updateIndexedObject(IndexedType indexedType, String id, String object);
	public void removeIndexedObject(IndexedType indexedType, String id);
	public SearchResponse findAll(IndexedType indexedType, Pageable pageable);
	public SearchResponse findAll(IndexedType indexedType, Pageable pageable, DateRangeFilter dateRangeFilter, Aggregation aggregation);
	public SearchResponse multiFieldSearch(IndexedType indexedType, List<ElasticSearchQuery> queries, boolean highlightHits, Pageable pageable);
	public SearchResponse multiFieldSearch(IndexedType indexedType, List<ElasticSearchQuery> queries, boolean highlightHits, Pageable pageable, DateRangeFilter dateRangeFilter);
	public SearchResponse phrasePrefixSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields);
	public SearchResponse phrasePrefixSearch(IndexedType indexedType, String searchText, Pageable pageable, DateRangeFilter dateRangeFilter, String... fields);
	public SearchResponse exactSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields);
	public SearchResponse fuzzySearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields);
	public SearchResponse crossFieldsSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields);	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query);	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, DateRangeFilter dateRangeFilter);
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, Pageable pageable, String... hitHighlightFields);
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, Pageable pageable, Aggregation aggregation, String... hitHighlightFields);	
	public void reindex(IndexedType indexedType);
	public void reindex(IndexedType indexedType, List<? extends Identifiable> objects);
	public void reindexAllSearchIndexes();
	public void delete(IndexedType indexedType, String id);
	public void delete(IndexedType indexedType, List<Integer> ids);	
}
