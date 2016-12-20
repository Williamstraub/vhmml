package org.vhmml.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.vhmml.dto.elasticsearch.Aggregation;
import org.vhmml.dto.elasticsearch.DateRangeFilter;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.IndexedField;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.dto.elasticsearch.MultiMatchQuery;
import org.vhmml.dto.elasticsearch.RangeQuery;
import org.vhmml.dto.elasticsearch.VhmmlIndexFields;
import org.vhmml.entity.Identifiable;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

	private static final Logger LOG = Logger.getLogger(ElasticSearchServiceImpl.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${elastic.search.host}")
	private String elasticSearchHost;
	
	@Value("${elastic.search.port}")
	private Integer elasticSearchPort;
	
	@Autowired
	private LexiconService lexiconService;
	
	@Autowired
	private ReferenceService referenceService;
	
	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private AuthorityListService authorityListService;
	
	@Autowired
	private FolioService folioService;
	
	public static final String CLUSTER_NAME = "vhmmlsearch";
	
	public enum Index {
		SEARCH("vhmmlsearch"),
		REPORT("vhmmlreport");
		
		private String name;
		
		Index(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	};
	
	// this is a list of methods, by index type, that are used to get all objects of a certain type from the database for re-indexing
	// for example, the for "lexicon_term" object type, the loader method is LexiconService.findAll
	private Map<IndexedType, Callable<List<? extends Identifiable>>> indexedTypeLoaders = new HashMap<>();
		
	private static enum StopWordFilter {
		ENGLISH("english_stop", "_english_"),
		GERMAN("german_stop", "_german_"),
		ITALIAN("italian_stop", "_italian_"),
		FRENCH("french_stop", "_french_");
		
		private String name;
		private String listName;
		
		StopWordFilter(String name, String listName) {
			this.name = name;
			this.listName = listName;
		}
		
		static List<String> getFilterNames() {
			List<String> filterNames = new ArrayList<String>();
			
			for(StopWordFilter filter : StopWordFilter.values()) {
				filterNames.add(filter.name);
			}
			
			return filterNames;
		}
	};
	
	private static enum SynonymFilter {
		BRITISH_ENGLISH("british_english", "british_english_synonyms.txt"),
		VHMML_SYNONYMS("vhmml_synonyms", "vhmml_synonyms.txt");
		
		private String name;
		private String synonymsFile;
		
		SynonymFilter(String name, String synonymsFile) {
			this.name = name;
			this.synonymsFile = synonymsFile;
		}
		
		static List<String> getFilterNames() {
			List<String> filterNames = new ArrayList<String>();
			
			for(SynonymFilter filter : SynonymFilter.values()) {
				filterNames.add(filter.name);
			}
			
			return filterNames;
		}
	};
	
	public ElasticSearchServiceImpl() {

		indexedTypeLoaders.put(IndexedType.LEXICON_TERM,
			new Callable<List<? extends Identifiable>>() {
				public List<? extends Identifiable> call() {
					return lexiconService.findAll();
				}
			}
		);
		
		indexedTypeLoaders.put(IndexedType.REFERENCE_ITEM,			
			new Callable<List<? extends Identifiable>>() {
				public List<? extends Identifiable> call() {
					return referenceService.findAll();
				}
			}
		);
		
		indexedTypeLoaders.put(IndexedType.READING_ROOM_OBJECT, 			
			new Callable<List<? extends Identifiable>>() {
				public List<? extends Identifiable> call() {
					return readingRoomService.getAllOverviews();
				}
			}
		);
		
		indexedTypeLoaders.put(IndexedType.READING_ROOM_CONTRIBUTOR,			
			new Callable<List<? extends Identifiable>>() {
				public List<? extends Identifiable> call() {
					return authorityListService.getAllContributors();
				}
			}
		);
		
		indexedTypeLoaders.put(IndexedType.FOLIO_OBJECT, 			
			new Callable<List<? extends Identifiable>>() {
				public List<? extends Identifiable> call() {
					return folioService.findAll();
				}
			}
		);		
	}
	
	public void initializeIndex(Index index) {
		String indexName = index.getName();
		Client client = getClient(CLUSTER_NAME);
		
		try {
			IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
	    	
	    	if(!res.isExists()) {	
	    		LOG.info("ElasticSearch index " + indexName + " doesn't exist, creating it...");
	    		CreateIndexRequestBuilder createIndexRequest = client.admin().indices().prepareCreate(indexName);
	    	
	    		createIndexRequest.setSettings(getIndexSettings());
	    		createIndexRequest.execute().actionGet();
	    		LOG.info("Index created successfully.");
	    	}
	    	
	    	List<IndexedType> types = IndexedType.getTypesForIndex(index);
	    	
	    	for(IndexedType type : types) {
    			String typeName = type.getName();
	    		TypesExistsResponse typeExistsResponse = client.admin().indices().prepareTypesExists(indexName).setTypes(typeName).execute().actionGet();

	    		if(!typeExistsResponse.isExists()) {
	    			LOG.info("Type mapping for type " + typeName + " doesn't exist in index, creating it...");
	    			
	    			client.admin().indices()
		                .preparePutMapping(indexName)
		                .setType(typeName)
		                .setSource(getIndexedObjectMapping(type.getFields()))
		                .execute().actionGet();    			
	    			LOG.info("Mapping for type " + typeName + " created successfully.");
	    		}	
	    	} 
		} finally {
			closeResources(client, null);
		}		   	
	}
	
	public void reindexAllSearchIndexes() {
		String indexName = Index.SEARCH.getName();
		Client client = getClient(CLUSTER_NAME);
		
		try {			
			boolean indexExists = client.admin().indices().prepareExists(indexName).execute().actionGet().isExists();
			
			if(indexExists) {
				client.admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
			}
			
			initializeIndex(Index.SEARCH);
			List<IndexedType> types = IndexedType.getTypesForIndex(Index.SEARCH);
			
			for(IndexedType type : types) {				
				reindex(type);
			}
		} finally {			
			closeResources(client, null);
		}	
		
	}
	
	public void delete(IndexedType indexedType, String id) {
		Client client = getClient(CLUSTER_NAME);
		
		try {
			client.prepareDelete(indexedType.getIndex().getName(), indexedType.getName(), id).execute().actionGet();
		} finally {			
			closeResources(client, null);
		}	
	}
	
	public void deleteType(IndexedType indexedType) {
		Client client = getClient(CLUSTER_NAME);
		
		try {
			DeleteMappingRequest deleteMapping = new DeleteMappingRequest(indexedType.getIndex().getName()).types(indexedType.getName());
			client.admin().indices().deleteMapping(deleteMapping).actionGet();
			// re-initialize the index so the mapping for the type we just deleted will get created again
			initializeIndex(indexedType.getIndex());
		} finally {			
			closeResources(client, null);
		}	
	}	
	
	public void reindex(IndexedType indexedType) {
		try {
			Callable<List<? extends Identifiable>> typeLoader = indexedTypeLoaders.get(indexedType);
			reindex(indexedType, typeLoader.call());
		} catch(Exception e) {
			throw new RuntimeException("Unexpected exception attempting to load all objects for indexed type " + indexedType.getName() + ", index may be out of sync", e);
		}		
	}
	
	public void delete(IndexedType indexedType, List<Integer> ids) {
		LOG.info("deleting objects of type " + indexedType.getName());
		Long start = new Date().getTime();
		Client client = getClient(CLUSTER_NAME);
		BulkProcessor bulkProcessor = null;
		
		try {
			bulkProcessor = BulkProcessor.builder(
		        client,  
		        new BulkProcessor.Listener() {			            
		            public void beforeBulk(long executionId, BulkRequest request) {
		            	LOG.info("before bulk delete...");
		            } 
		            
		            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
		            	LOG.info("after bulk delete...");
		            } 

		            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
		            	LOG.error("Bulk delete failed, exception during bulk delete.", failure);
		            	throw new RuntimeException(failure);
		            } 
		        }).build();
			
			for(Integer id : ids) {			
				bulkProcessor.add(new DeleteRequest(indexedType.getIndex().getName(), indexedType.getName(), id.toString()));
			}
		} finally {			
			closeResources(client, bulkProcessor);	
			LOG.info("bulk delete took " + (new Date().getTime() - start));
		}
	}
	
	public void reindex(IndexedType indexedType, List<? extends Identifiable> objects) {
		LOG.info("re-indexing type " + indexedType.getName() + ", " + objects.size() + " total objects");
		Long start = new Date().getTime();
		Client client = getClient(CLUSTER_NAME);
		BulkProcessor bulkProcessor = null;
		
		try {
			// delete the mapping so the indexed data matches the database
			deleteType(indexedType);
			bulkProcessor = BulkProcessor.builder(
		        client,  
		        new BulkProcessor.Listener() {			            
		            public void beforeBulk(long executionId, BulkRequest request) {
		            	LOG.info("before bulk...");
		            } 
		            
		            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
		            	LOG.info("after bulk...");
		            } 

		            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
		            	LOG.error("Re-index failed, exception during bulk index update.", failure);
		            	throw new RuntimeException(failure);
		            } 
		        }).build();
			
			for(Identifiable object : objects) {			
				bulkProcessor.add(new IndexRequest(indexedType.getIndex().getName(), indexedType.getName(), object.getId().toString()).source(objectMapper.writeValueAsString(object)));
			}
		} catch(IOException e) {
			LOG.error("IOException running " + indexedType.getName() + " re-index", e);
		} finally {			
			closeResources(client, bulkProcessor);	
			LOG.info("re-index of type " + indexedType + " took " + (new Date().getTime() - start));
		}
	}
	
	public void updateIndexedObject(IndexedType indexedType, String id, String object) {
		
		Client client = getClient(CLUSTER_NAME);
		
		try {			
			client.prepareIndex(indexedType.getIndex().getName(), indexedType.getName(), id)
				.setSource(object)
				.execute()
				.actionGet();
		} catch(Exception e) {
			LOG.error("Exception trying to update indexed " + indexedType.getName() + " for id " + id);
		} finally {
			closeResources(client, null);
		}	
	}
	
	public void removeIndexedObject(IndexedType indexedType, String id) {
		Client client = getClient(CLUSTER_NAME);
		
		try {			
			client.prepareDelete(indexedType.getIndex().getName(), indexedType.getName(), id)
				.execute()
				.actionGet();
		} finally {
			closeResources(client, null);
		}	
	}

	public SearchResponse findAll(IndexedType indexedType, Pageable pageable, DateRangeFilter dateRangeFilter, Aggregation aggregation) {
		QueryBuilder query = addQueryDateFilter(QueryBuilders.matchAllQuery(), dateRangeFilter);
		
		return executeQuery(indexedType, query, pageable, aggregation, new String[]{});
	}
	
	public SearchResponse findAll(IndexedType indexedType, Pageable pageable) {
		return findAll(indexedType, pageable, null, null);
	}
	
	public SearchResponse multiFieldSearch(IndexedType indexedType, List<ElasticSearchQuery> queries, boolean highlightHits, Pageable pageable, DateRangeFilter dateRangeFilter) {
		QueryBuilder boolQuery = QueryBuilders.boolQuery();
		Map<String, String> hitHighlightFieldsMap = new HashMap<>();
		
		for(ElasticSearchQuery query : queries) {
			switch(query.getParentBoolOperation())  {
				case MUST:
					((BoolQueryBuilder)boolQuery).must(query.getQuery());
					break;
				case MUST_NOT:
					((BoolQueryBuilder)boolQuery).mustNot(query.getQuery());
					break;
				case SHOULD:
					((BoolQueryBuilder)boolQuery).should(query.getQuery());
					break;
			}
						
			Class<? extends ElasticSearchQuery> queryClass = query.getClass();
			
			if(highlightHits) {
				// terms like a "term" query aren't part of highlighted search results because they're an exact match on a static value
				// like "objectType=MANUSCRIPT", which is really just a filter so it looks weird to have them highlighted in a search result
				if(MultiMatchQuery.class.isAssignableFrom(queryClass)) {
					String[] fields = ((MultiMatchQuery)query).getFields();
					for(String field : fields) {
						hitHighlightFieldsMap.put(field, field);
					}
				} else if(RangeQuery.class.isAssignableFrom(queryClass)) {
					String fieldName = ((RangeQuery)query).getField();
					hitHighlightFieldsMap.put(fieldName, fieldName);
				}
			}					
		}
		
		String[] hitHighlightFields = hitHighlightFieldsMap.keySet().toArray(new String[hitHighlightFieldsMap.size()]);
		
		if(dateRangeFilter != null) {
			boolQuery = addQueryDateFilter(boolQuery, dateRangeFilter);
		}
		
		return executeQuery(indexedType, boolQuery, pageable, hitHighlightFields);
	}
	
	public SearchResponse multiFieldSearch(IndexedType indexedType, List<ElasticSearchQuery> queries, boolean highlightHits, Pageable pageable) {		
		return multiFieldSearch(indexedType, queries, highlightHits, pageable, null);
	}	
	
	public SearchResponse crossFieldsSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields) {
		// note that setting slop value of 2 allows 2 term moves to still be considered a match, i.e.
		// a search for "nabia abbot" will return documents written by "abbot, nabia"
		QueryBuilder query = QueryBuilders.multiMatchQuery(searchText, fields).type(Type.CROSS_FIELDS).operator(Operator.AND).slop(2);		
		return executeQuery(indexedType, query, pageable, fields);
	}
	
	public SearchResponse phrasePrefixSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields) {
		return phrasePrefixSearch(indexedType, searchText, pageable, null, fields);
	}
	
	public SearchResponse phrasePrefixSearch(IndexedType indexedType, String searchText, Pageable pageable, DateRangeFilter dateRangeFilter, String... fields) {
		// note that setting slop value of 2 allows 2 term moves to still be considered a match, i.e.
		// a search for "nabia abbot" will return documents written by "abbot, nabia"
		QueryBuilder query = QueryBuilders.multiMatchQuery(searchText, fields).type(Type.PHRASE_PREFIX).slop(2);
		
		if(dateRangeFilter != null) {
			query = addQueryDateFilter(query, dateRangeFilter);
		}
		
		return executeQuery(indexedType, query, pageable, fields);
	}
	
	public SearchResponse exactSearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields) {		
		QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(searchText, fields);			
		return executeQuery(indexedType, matchQuery, pageable, fields);
	}	
	
	public SearchResponse fuzzySearch(IndexedType indexedType, String searchText, Pageable pageable, String... fields) {
		// combine the fuzzy search with a boosted exact match search so that any exact matches rank highest
		int fuzziness = searchText.length() > 8 ? 2 : 1;
		QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(searchText, fields).boost(10.0f);
		QueryBuilder fuzzyQuery = QueryBuilders.fuzzyLikeThisQuery(fields).fuzziness(Fuzziness.fromEdits(fuzziness)).likeText(searchText);			
		QueryBuilder boolQuery = QueryBuilders.boolQuery().should(matchQuery).should(fuzzyQuery);		

		return executeQuery(indexedType, boolQuery, pageable, fields);
	}
	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query) {
		return executeQuery(indexedType, query, null, null, new String[]{});
	}
	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, DateRangeFilter dateRangeFilter) {
		if(dateRangeFilter != null) {
			query = addQueryDateFilter(query, dateRangeFilter);
		}
		
		return executeQuery(indexedType, query, null, new String[]{});
	}
	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, Pageable pageable, String... hitHighlightFields) {
		return executeQuery(indexedType, query, pageable, null, hitHighlightFields);
	}
	
	public SearchResponse executeQuery(IndexedType indexedType, QueryBuilder query, Pageable pageable, Aggregation aggregation, String... hitHighlightFields) {
		SearchResponse searchResponse = null;	
		Client client = getClient(CLUSTER_NAME);
		
		try {						
			SearchRequestBuilder searchRequestBuilder = client
				.prepareSearch(indexedType.getIndex().getName())
				.setTypes(indexedType.getName())
				.setQuery(query);

			for (String field : hitHighlightFields) {
				searchRequestBuilder.addHighlightedField(field).setHighlighterPreTags("<em class=\"highlight\">");
			}

			// if there's an aggregation, we only sort the aggregation because we're not consuming the query results
			if(aggregation != null) {
				setQueryAggregation(searchRequestBuilder, aggregation.getAggregationField(), aggregation.getAggregationMax(), pageable);				 
			} else {
				if(pageable != null) {
					setQuerySort(pageable, searchRequestBuilder, indexedType.getFields());
					searchRequestBuilder.setFrom(pageable.getOffset());
					searchRequestBuilder.setSize(pageable.getPageSize());
				}				
			}			
			
			searchResponse = searchRequestBuilder.execute().actionGet();
		} finally {
			closeResources(client, null);
		}

		return searchResponse;
	}
	
	public QueryBuilder addQueryDateFilter(QueryBuilder queryBuilder, DateRangeFilter dateRangeFilter) {
		QueryBuilder query = queryBuilder;
		
		if(dateRangeFilter != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(dateRangeFilter.getDateFormat());
			String startDate = formatter.format(dateRangeFilter.getStart());
			String endDate = formatter.format(dateRangeFilter.getEnd());
			FilterBuilder dateFilter = new RangeFilterBuilder(dateRangeFilter.getDateField()).gte(startDate).lte(endDate);
			query = QueryBuilders.filteredQuery(queryBuilder, dateFilter);
		}
		 		
		return query;
	}
	
	private void setQuerySort(Pageable pageable, SearchRequestBuilder searchRequestBuilder, List<IndexedField> indexFields) {
		// TODO: now that we're not hitting the database might want to think about factoring out the Spring Pageable
		// logic, it's very handy for paging/sorting with database queries but with the addition of ElasticSearch
		// it's becoming a confusing translation layer
		
		if(pageable != null) {
			Sort sort = pageable.getSort();
			
			if(sort != null) {
				
				Iterator<Sort.Order> sortFields = pageable.getSort().iterator();
				
				while(sortFields.hasNext()) {
					Sort.Order sortField = sortFields.next();
					SortOrder sortDirection = sortField.getDirection().equals(Direction.ASC) ? SortOrder.ASC : SortOrder.DESC;
					// we always create a raw field for sorting, the regular fields are run through the analyzer and therefore broken up into individual words				
					boolean indexContainsSortField = VhmmlIndexFields.containsField(indexFields, sortField.getProperty());
					String sortProperty = indexContainsSortField ? sortField.getProperty() + ".raw" : sortField.getProperty();				
					searchRequestBuilder.addSort(sortProperty, sortDirection);				
				}	
			}
		}		
	}
	
	private void setQueryAggregation(SearchRequestBuilder searchRequestBuilder, String aggregationField, Integer aggregationMax, Pageable pageable) {
		if(StringUtils.isNotEmpty(aggregationField)) {
			// we always aggregate on the raw field because we don't want aggregation of the tokens, just the original untouched value
			TermsBuilder aggregation = AggregationBuilders
				.terms(aggregationField)
				.field(aggregationField + ".untouched")
				.size(aggregationMax);
			
			if(pageable != null && pageable.getSort() != null) {
				Order termSort = pageable.getSort().getOrderFor("_term");
				Order countSort = pageable.getSort().getOrderFor("_count");
				
				if(termSort != null) {
					aggregation.order(Terms.Order.term(termSort.isAscending()));
				} else if(countSort != null) {
					aggregation.order(Terms.Order.count(countSort.isAscending()));
				}
			}
			searchRequestBuilder.addAggregation(aggregation);
		}
	}
	
	@SuppressWarnings("resource")
	private Client getClient(String clusterName) {
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
		TransportClient transportClient = new TransportClient(settings);
		return transportClient.addTransportAddress(new InetSocketTransportAddress(elasticSearchHost, elasticSearchPort));
	}
	
	private void closeResources(Client client, BulkProcessor bulkProcessor) {		
		if (bulkProcessor != null) {
			try {
				bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
			} catch(InterruptedException e) {
				LOG.error("Unable to close ElasticSearch BulkProcessor", e);
			}			
		}
		
		if (client != null) {
			client.close();			
		}
	}
	
	private void addStopWordFilter(XContentBuilder mapping, StopWordFilter stopWordFilter) throws IOException {
		mapping.startObject(stopWordFilter.name)
				.field("type", "stop")
				.array("stopwords", stopWordFilter.listName)
			.endObject();
	}
	
	private void addSynonymFilter(XContentBuilder mapping, SynonymFilter synonymFilter) throws IOException {
		mapping.startObject(synonymFilter.name)
			.field("type", "synonym")
			.field("synonyms_path", synonymFilter.synonymsFile)
		.endObject();
	}
	
	private XContentBuilder getIndexSettings() {
		XContentBuilder mapping = null;
		// sorting filter is the simplest, only lowercase & diacritic insensitive, no stop words, stemming, etc.
		List<String> sortingFilters = new ArrayList<String>();
		sortingFilters.add("lowercase");
		sortingFilters.add("icu_folding");
		
		// basic filters add special character filter, this is used for exact phrase match when user puts quotes around the search text
		List<String> basicFilters = new ArrayList<String>();
		basicFilters.addAll(sortingFilters);
		basicFilters.add("special_char_splitter");
		
		// small n gram folding is just a basic filter that adds a 3-5 edge ngram filter for contains matches
		// this is used for things like hmml project number (we need contains matches and we don't want things like cet getting removed due to stop word filters)
		List<String> smallNGramFoldingAnalyzerFilters = new ArrayList<String>();		
		smallNGramFoldingAnalyzerFilters.addAll(basicFilters);
		smallNGramFoldingAnalyzerFilters.add("small_ngram");
		
		// zero folding is just has basic filters but adds a filter to strip leading zeros from tokens
		// this is used for fields like hmml project number, where we need to match on things like 00001 & 1 and not filter out things like cet due to stop words
		List<String> zeroTrimFoldingAnalyzerFilters = new ArrayList<String>();
		zeroTrimFoldingAnalyzerFilters.addAll(basicFilters);
		zeroTrimFoldingAnalyzerFilters.add("leading_zero_trim");
		
		List<String> foldingAnalyzerFilters = new ArrayList<String>();
		// folding analyzer that doesn't do stemming, we use this for things like boolean fields (we don't want to stem values like "false" into "fals"
		foldingAnalyzerFilters.addAll(basicFilters);	
		foldingAnalyzerFilters.addAll(StopWordFilter.getFilterNames());	
		foldingAnalyzerFilters.addAll(SynonymFilter.getFilterNames());

		// english stemmer reduces words to their root form so we match on things like plural/non-plural, etc.
		List<String> stemmingFoldingAnayzerFilters = new ArrayList<String>();
		stemmingFoldingAnayzerFilters.addAll(foldingAnalyzerFilters);
		stemmingFoldingAnayzerFilters.add("english_stemmer");

		// default analyzer filters out stop words and adds synonym filter and does not include stemming filter
		List<String> defaultAnalyzerFilters = StopWordFilter.getFilterNames();
		defaultAnalyzerFilters.addAll(SynonymFilter.getFilterNames());
		
		// stemmming analyzer that's the same as the main analyzer but adds an english stemmer so we can get matches for plural/singular, etc.
		// we only use this on fields where we control the list, like features, because if we use it on all fields we get some odd results for free text entry searches
		List<String> stemmingAnayzerFilters = new ArrayList<String>();
		stemmingAnayzerFilters.addAll(defaultAnalyzerFilters);
		stemmingAnayzerFilters.add("english_stemmer");
		
		// small n gram is the same as main analyzer but adds a 3-5 edge ngram filter for contains matches
		List<String> smallNGramAnalyzerFilters = new ArrayList<String>();
		smallNGramAnalyzerFilters.addAll(defaultAnalyzerFilters);
		smallNGramAnalyzerFilters.add("small_ngram");		

		// zero trim is the same as main analyzer but adds a filter to strip leading zeros from tokens
		List<String> zeroTrimAnalyzerFilters = new ArrayList<String>();
		zeroTrimAnalyzerFilters.addAll(defaultAnalyzerFilters);
		zeroTrimAnalyzerFilters.add("leading_zero_trim");		
				
		try {
			mapping = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("analysis")
						.startObject("char_filter")
			                .startObject("sorting_char_filter")
			                    .field("type", "mapping")
			                    .array("mappings", "\"=>", "\'=>") // remove quotes for sorting
			                .endObject()			                			               
			            .endObject()
						.startObject("filter");
			
							for(StopWordFilter filter : StopWordFilter.values()) {
								addStopWordFilter(mapping, filter);
							}
							
							for(SynonymFilter filter : SynonymFilter.values()) {
								addSynonymFilter(mapping, filter);
							}
							
							mapping.startObject("english_stemmer")
								.field("type", "stemmer")
								.field("language", "english")
							.endObject()
							
							// small n gram is used for fields like hmml project number because we need only a few characters
							// to consider something a partial match, i.e. ste needs to match stef 00218
							.startObject("small_ngram")
								.field("type", "edge_ngram")
								.field("min_gram", 3)
								.field("max_gram", 8)
							.endObject()
							
							.startObject("leading_zero_trim")
								.field("type", "pattern_replace")
								.field("pattern", "^0+(.*)")
								.field("replacement", "$1")
							.endObject()
							
							// filter to split words on special characters, in other languages words are often separated by characters other than space
							// for example d'Amiens is actually two words so we need it tokenized as "d" "Amiens"
							.startObject("special_char_splitter")
			                    .field("type", "word_delimiter")
			                    .field("preserve_original", "true")
			                .endObject()

						.endObject()
						
						.startObject("analyzer")
							.startObject("basic_analyzer")
								.field("type", "custom")								
		                      	.array("filter", basicFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "standard")
		                    .endObject()
							.startObject("default_analyzer")
								.field("type", "custom")								
		                      	.array("filter", defaultAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "standard")
		                    .endObject()
		                    .startObject("stemming_analyzer")
								.field("type", "custom")								
		                      	.array("filter", stemmingAnayzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "standard")
		                    .endObject()
		                    .startObject("folding_analyzer")
								.field("type", "custom")								
		                      	.array("filter", foldingAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "icu_tokenizer")
		                    .endObject()
		                    .startObject("stemming_folding_analyzer")
								.field("type", "custom")								
		                      	.array("filter", stemmingFoldingAnayzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "icu_tokenizer")
		                    .endObject()
		                    .startObject("small_ngram_analyzer")
								.field("type", "custom")								
		                      	.array("filter", smallNGramAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "standard")
		                    .endObject()
		                    .startObject("small_ngram_folding_analyzer")
								.field("type", "custom")								
		                      	.array("filter", smallNGramFoldingAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "icu_tokenizer")
		                    .endObject()		                    
		                    .startObject("zero_trim_analyzer")
								.field("type", "custom")								
		                      	.array("filter", zeroTrimAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "standard")
		                    .endObject()
		                    .startObject("zero_trim_folding_analyzer")
								.field("type", "custom")								
		                      	.array("filter", zeroTrimFoldingAnalyzerFilters.toArray())
		                      	.array("char_filter", "html_strip")
		                      	.field("tokenizer", "icu_tokenizer")
		                    .endObject()		                    
		                    
		                    .startObject("sorting_analyzer") // create an analyzer for sorting that doesn't break up the field into tokens 
								.field("type", "custom")
		                      	.field("tokenizer", "keyword")		                      	
		                      	.array("char_filter", "sorting_char_filter", "html_strip")		                      	
		                      	.array("filter", sortingFilters.toArray())		                          
		                    .endObject()		                    
		                .endObject()
		            .endObject()
			    .endObject();			                      
		} catch (IOException e) {
			LOG.error("IOException attempting to assemble index settings", e);
		}
		
		return mapping;
	}
	
	private void addIndexedFieldMapping(XContentBuilder mapping, IndexedField field) throws IOException {
		
		mapping.startObject(field.getName())
			.field("type", field.getType());
			if(field.getType().equals("date") && StringUtils.isNotEmpty(field.getFormat())) {
				mapping.field("format", field.getFormat());
			}
			// term_vector = with_positions_offsets makes highlighting fragments break at words so you don't end up with things like a highlight fragment starting with punctuation
			mapping.field("term_vector", "with_positions_offsets")
			.field("analyzer", field.getAnalyzer())			
			.startObject("fields");
		
				// field variants is a map of field variations to be indexed for this particular field, the 
				// map key is the name of the field variant and the value is the analyzer to use for the field
				for(String fieldVariant : field.getFieldVariants().keySet()) {					
					mapping.startObject(fieldVariant)
						.field("type", field.getType());
						
						if(field.getType().equals("date") && StringUtils.isNotEmpty(field.getFormat())) {
							mapping.field("format", field.getFormat());
						}
						
						if("untouched".equals(fieldVariant)) {
							mapping.field("index", "not_analyzed");
						} else {
							mapping.field("analyzer", field.getFieldVariants().get(fieldVariant));
						}						
					mapping.endObject();
				}
				
			mapping.endObject()
		.endObject();
	}
	
	private XContentBuilder getIndexedObjectMapping(List<IndexedField> fields) {
		XContentBuilder mapping = null;
		
		try {			
			mapping = XContentFactory.jsonBuilder()
				.startObject()
					.startObject("properties");
						for(IndexedField field : fields) {
							addIndexedFieldMapping(mapping, field);							
						}						
					mapping.endObject();
				mapping.endObject();
		} catch (IOException e) {
			LOG.error("IOException attempting to assemble index settings", e);
		}
		
		return mapping;
	}
}
