package org.vhmml.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.vhmml.dto.FolioSearchHit;
import org.vhmml.dto.FolioSearchResult;
import org.vhmml.dto.elasticsearch.BoolMultiMatchQuery;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery.BoolOperation;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.dto.elasticsearch.MultiMatchQuery;
import org.vhmml.dto.elasticsearch.MultipleFieldRangeQuery;
import org.vhmml.dto.elasticsearch.TermQuery;
import org.vhmml.dto.elasticsearch.WildcardQuery;
import org.vhmml.entity.FolioObject;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.repository.FolioObjectRepository;
import org.vhmml.security.VhmmlSecurityUtil;
import org.vhmml.util.AttributeUtil;
import org.vhmml.util.ElasticSearchUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FolioServiceImpl implements FolioService {

	private static final Logger LOG = Logger.getLogger(FolioServiceImpl.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	// This enum is the list of Folio fields that are searched as part of the keyword search, as opposed to fields that are put in the index - we put fields like icon
	// name in the index so they are part of a search result but we don't actually want to search those fields.
	protected enum SearchField {
		COUNTRY("Country", "country"),
		CITY("City", "city"),		
		REPOSITORY("Repository", "repository"),
		SHELFMARK("Shelfmark", "shelfMark"),
		COMMON_NAME("Common Name", "commonName"),
		PROVENANCE("Provenance", "provenance"),
		BIBLIOGRAPHY("Bibliography", "bibliography"),
		PLACE_OF_ORIGIN("Place Of Origin", "placeOfOrigin"),
		LANGUAGE("Language", "language"),
		WRITINGSYSTEM("Writing System", "writingSystem"),
		SCRIPT("Script", "script"),
		TITLE("Title", "title"),
		TEXT("Text", "text"),
		DESCRIPTION("Description", "description"),
		SPECIALFEATURES("Special Features", "specialFeatures"),
		TRANSCRIPTION("Transcription", "transcription");

		private String displayName;
		private	String field;
		
		SearchField(String displayName, String field) {
			this.displayName = displayName;
			this.field = field;			
		}		
		
		public static String[] getAllFields() {
			List<String> allFields = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				allFields.addAll(field.getIndexedFields());
			}
			
			return allFields.toArray(new String[allFields.size()]);
		}
		
		// basic fields are case insensitive & diacritic insensitive only, no stemming, stop words, etc.
		// they are used for exact phrase search when the user surrounds the search text with quotes
		public static String[] getBasicFields() {
			List<String> basicFields = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				basicFields.add(field.field + ".basic");
			}
			
			return basicFields.toArray(new String[basicFields.size()]);
		}
		
		public static SearchField forName(String name) {
			SearchField searchField = null;
        	
        	for(SearchField nextField : SearchField.values()) {
        		if(name != null && nextField.getField().equals(name)) {
        			searchField = nextField;
        			break;
        		}
        	}        	
        	
        	return searchField;
        }
		
		public static Map<String, String> getDisplayNameMap() {
			Map<String, String> displayNameMap = new HashMap<>();
			
			for(SearchField field : SearchField.values()) {
				displayNameMap.put(field.field, field.getDisplayName());
			}
			
			return displayNameMap;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String getField() {
			return field;
		}
		
		// indexed fields contains all versions of the field that are indexed, i.e. title, title.folded, etc. 
		public List<String> getIndexedFields() {
			List<String> indexedFields = new ArrayList<>();
			
			indexedFields.add(field);
			indexedFields.add(field + ".folded");
			
			return indexedFields;
		}
	}    		    
	
	@Autowired
	private FolioObjectRepository folioObjectRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;	
	
	public FolioObject find(Long objectId) {
		FolioObject object = folioObjectRepository.findOne(objectId);
		object.setEditable(currentUserCanEdit(object));
		return object;
	}
	
	public FolioObject findByObjectNumber(String objectNumber) {
		FolioObject object = folioObjectRepository.findByFolioObjectNumber(objectNumber);
		object.setEditable(currentUserCanEdit(object));
		return object;
	}
	
	public List<FolioObject> findAll() {		
		return Lists.newArrayList(folioObjectRepository.findAll());
	}
	
	public List<String> findLanguages() {
		return getUniqueList(folioObjectRepository.findLanguages());		
	}
	
	public List<String> findWritingSystems() {
		return getUniqueList(folioObjectRepository.findWritingSystems());
	}
	
	public List<String> findScripts() {
		return getUniqueList(folioObjectRepository.findScripts());
	}
	
	private List<String> getUniqueList(List<String> allValues) {
		Map<String, String> uniqueValues = new HashMap<>();
		
		for(String valueString : allValues) {
			List<String> valueList = AttributeUtil.getStringAttributeAsList(valueString, ";");
			
			for(String value : valueList) {
				value= value.trim();
				uniqueValues.put(value, value);
			}
		}
		List<String> uniqueList = Lists.newArrayList(uniqueValues.values());
		Collections.sort(uniqueList);
		
		return uniqueList;
	}
	
	@Transactional
	public FolioObject save(FolioObject folioObject) {			
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUser = user.getUsername();
		Date now = new Date();
		folioObject.setLastUpdatedBy(currentUser);
		folioObject.setLastUpdate(now);
		
		if(folioObject.getId() == null) {
			folioObject.setCreatedBy(currentUser);
			folioObject.setCreatedDate(now);
		}
		
		FolioObject updatedObject = folioObjectRepository.save(folioObject);
		
		try {
			elasticSearchService.updateIndexedObject(IndexedType.FOLIO_OBJECT, updatedObject.getId().toString(), objectMapper.writeValueAsString(updatedObject));
		} catch (JsonProcessingException e) {
			LOG.error("JsonProcessingException while trying to update index after updating a folio object, index will be out of sync with the database", e);
		}			
		
		return updatedObject;
	}
	
	@Transactional
	public void delete(Long objectId) {		
		FolioObject object = find(objectId);
		
		if(object != null) {
			folioObjectRepository.delete(object);
			elasticSearchService.delete(IndexedType.FOLIO_OBJECT, objectId.toString());
		}		 
	}
	
	public FolioSearchResult search(Map<String, String> searchTerms, Pageable pageable) {	
		List<ElasticSearchQuery> elasticSearchQueries = new ArrayList<>();
		boolean highlightHits = false;
		
		// if the current user isn't a cataloger, they only see active records
		if(!VhmmlSecurityUtil.isCurrentUserCataloger()) {
			elasticSearchQueries.add(new TermQuery("active", "true"));
		}
		
		for(String field : searchTerms.keySet()) {
			String searchString = searchTerms.get(field);

			if(StringUtils.isNotEmpty(searchString)) {
				
				switch(field) {
					case "keyword":
						boolean exactSearch = searchString.startsWith("\"") && searchString.endsWith("\"");
						boolean wildcardSearch = searchString.contains("*");
						if(exactSearch) {
							elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getAllFields(), MultiMatchQueryBuilder.Type.PHRASE, 0));
						} else if(wildcardSearch) {
							elasticSearchQueries.add(new WildcardQuery(searchString, SearchField.getAllFields()));
							// add an exact search for the search string with the wildcards stripped, this will make searches
							// for things like christ* push the hits with an exact match on "christ" to the top
							String strippedSeachString = searchString.replaceAll("\\*", "").replaceAll("\\?", "");
							elasticSearchQueries.add(new MultiMatchQuery(strippedSeachString, SearchField.getBasicFields(), MultiMatchQueryBuilder.Type.PHRASE, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, BoolOperation.SHOULD));
						} else {
							String[] searchStringTokens = StringUtils.split(searchString);
							// if they entered more than one term in the keyword field, then we do a "should" operation, meaning one of the search terms should appear in a field, (a "must" would require every search term to be in a single field)
							// if they only entered on search term, then we do a "must" operation because the search term must appear ina field, otherwise it's not filtering based on the search term they entered at all
							// because it doesn't necessarily have to match any fields to be considered a match
							BoolOperation boolOperation = searchStringTokens.length > 1 ? BoolOperation.SHOULD : BoolOperation.MUST;
							elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getBasicFields(), MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, boolOperation));						
							
						}
						
						highlightHits = true;
						break;
					case "beginDate":
						elasticSearchQueries.add(new MultipleFieldRangeQuery(searchTerms.get("endDate"), searchString, new String[] {"beginDate", "endDate"}));						
						break;
					case "endDate":
						// endDate gets handled with begin date
						break;
					case "active":											
						elasticSearchQueries.add(new TermQuery(field, searchString));
						break;					
					case "language":					
					case "script":
					case "writingSystem":
						List<String> indexedFields = SearchField.forName(field).getIndexedFields();
						
						elasticSearchQueries.add(
							new BoolMultiMatchQuery(
								indexedFields.toArray(new String[indexedFields.size()]), 
								searchString.split("</search-term>"), // we split on this weird token because fields like feature have things like commas in the values 
								MultiMatchQueryBuilder.Type.PHRASE, 
								org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND,
								BoolOperation.SHOULD));
						break;					
				}				
			}					
		}
		
		SearchResponse searchResponse = elasticSearchService.multiFieldSearch(
			IndexedType.FOLIO_OBJECT, 
			elasticSearchQueries, 
			highlightHits,
			pageable);
		        
		return getSearchResult(searchResponse, pageable);		
	}
	
	private FolioSearchResult getSearchResult(SearchResponse searchResponse, Pageable pageable) {
		FolioSearchResult searchResult = new FolioSearchResult();		
		SearchHit[] hits = searchResponse.getHits().getHits();
        List<FolioSearchHit> searchHits = new ArrayList<>();
        
        for(SearchHit hit : hits) {
        	Map<String, Object> source = hit.getSource();
        	Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        	FolioObject object = new FolioObject();
        	object.setId(new Long((Integer)source.get("id")));
        	object.setCreatedBy(ElasticSearchUtil.getFieldValue(source, highlightFields, "createdBy"));
        	object.setLastUpdatedBy(ElasticSearchUtil.getFieldValue(source, highlightFields, "lastUpdatedBy"));	
        	object.setFolioObjectNumber(ElasticSearchUtil.getFieldValue(source, highlightFields, "folioObjectNumber"));	
        	object.setIconName(ElasticSearchUtil.getFieldValue(source, highlightFields, "iconName"));	
        	object.setCountry(ElasticSearchUtil.getFieldValue(source, highlightFields, "country"));	
        	object.setCity(ElasticSearchUtil.getFieldValue(source, highlightFields, "city"));	
        	object.setRepository(ElasticSearchUtil.getFieldValue(source, highlightFields, "repository"));	
        	object.setShelfMark(ElasticSearchUtil.getFieldValue(source, highlightFields, "shelfMark"));
        	object.setCommonName(ElasticSearchUtil.getFieldValue(source, highlightFields, "commonName"));
        	object.setProvenance(ElasticSearchUtil.getFieldValue(source, highlightFields, "provenance"));
        	object.setBibliography(ElasticSearchUtil.getFieldValue(source, highlightFields, "bibliography"));
        	object.setExternalUrl(ElasticSearchUtil.getFieldValue(source, highlightFields, "externalUrl"));
        	object.setPermanentLink(ElasticSearchUtil.getFieldValue(source, highlightFields, "permanentLink"));
        	object.setAcknowledgements(ElasticSearchUtil.getFieldValue(source, highlightFields, "acknowledgements"));
        	object.setPlaceOfOrigin(ElasticSearchUtil.getFieldValue(source, highlightFields, "placeOfOrigin"));
        	object.setDatePrecise(ElasticSearchUtil.getFieldValue(source, highlightFields, "datePrecise"));
        	object.setBeginDate((Integer)source.get("beginDate"));
        	object.setEndDate((Integer)source.get("endDate"));
        	object.setDateCentury(ElasticSearchUtil.getFieldValue(source, highlightFields, "dateCentury"));
        	object.setLanguage(ElasticSearchUtil.getFieldValue(source, highlightFields, "language"));
        	object.setWritingSystem(ElasticSearchUtil.getFieldValue(source, highlightFields, "writingSystem"));
        	object.setScript(ElasticSearchUtil.getFieldValue(source, highlightFields, "script"));
        	object.setTitle(ElasticSearchUtil.getFieldValue(source, highlightFields, "title"));
        	object.setText(ElasticSearchUtil.getFieldValue(source, highlightFields, "text"));
        	object.setDescription(ElasticSearchUtil.getFieldValue(source, highlightFields, "description"));
        	object.setSpecialFeatures(ElasticSearchUtil.getFieldValue(source, highlightFields, "specialFeatures"));
        	object.setTranscription(ElasticSearchUtil.getFieldValue(source, highlightFields, "transcription"));
        	object.setActive((Boolean)source.get("active"));
    		object.setEditable(currentUserCanEdit(object));
    		
        	FolioSearchHit searchHit = new FolioSearchHit(object);
        	searchHit.setHighlightFields(ElasticSearchUtil.getHighlightFieldValues(source, highlightFields));
        	searchHits.add(searchHit);
        }		
		
        searchResult.setFieldDisplayNames(SearchField.getDisplayNameMap());
        searchResult.setSearchHits(searchHits);        
		searchResult.setPagingInformation(searchResponse, pageable);
		
		return searchResult;
	}
	
	public boolean currentUserCanEdit(FolioObject object) {
		boolean canEdit = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(auth != null && object != null && auth.getPrincipal().getClass().isAssignableFrom(User.class)) {
        	User user = (User)auth.getPrincipal();        	
        	boolean isOwner = user.getUsername().equals(object.getCreatedBy());
        	boolean canEditOwn = user.hasRole(Role.Name.ROLE_FOLIO_CREATOR);
        	boolean canEditAny = user.hasRole(Role.Name.ROLE_ADMIN);        	
       		canEdit = (canEditOwn && isOwner) || canEditAny;        	
        }		
		
		return canEdit;
	}
}
