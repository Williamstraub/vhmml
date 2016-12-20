package org.vhmml.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vhmml.dto.ReferenceSearchResult;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery.BoolOperation;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.dto.elasticsearch.MultiMatchQuery;
import org.vhmml.dto.zotero.ZoteroCollectionImportResult;
import org.vhmml.dto.zotero.ZoteroItemImportResult;
import org.vhmml.dto.zotero.ZoteroReferenceCollection;
import org.vhmml.dto.zotero.ZoteroReferenceGroup;
import org.vhmml.dto.zotero.ZoteroReferenceItem;
import org.vhmml.dto.zotero.ZoteroSearchResult;
import org.vhmml.entity.Creator;
import org.vhmml.entity.ReferenceCreator;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.entity.ReferenceEntry.DisplayField;
import org.vhmml.entity.ReferenceListItem;
import org.vhmml.entity.ReferenceTag;
import org.vhmml.entity.Tag;
import org.vhmml.repository.CreatorRepository;
import org.vhmml.repository.ReferenceEntryRepository;
import org.vhmml.repository.ReferenceListRepository;
import org.vhmml.repository.TagRepository;
import org.vhmml.service.ApplicationConfigService.Property;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

@Service
public class ReferenceServiceImpl implements ReferenceService {

	private static final Logger LOG = Logger.getLogger(ReferenceServiceImpl.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ReferenceEntryRepository referenceEntryRepository;
	
	@Autowired
	private ReferenceListRepository referenceListRepository;
	
	@Autowired
	private CreatorRepository creatorRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@Autowired
	private ApplicationConfigService configService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RemoteServiceUtil remoteServiceUtil;
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public List<ReferenceListItem> findAll() {
		return Lists.newArrayList(referenceListRepository.findAll());
	}
	
	public ReferenceSearchResult search(SearchType searchType, String searchString, Pageable pageable) {
		SearchResponse searchResponse = null;
		List<ElasticSearchQuery> elasticSearchQueries = new ArrayList<>();
		
		if(StringUtils.isEmpty(searchString) || searchType == null) {
			searchResponse = elasticSearchService.findAll(IndexedType.REFERENCE_ITEM, pageable);
		} else {
			String[] fields = null;
			
			switch(searchType) {
				case AUTHOR:
					fields = new String[] {"author", "author.folded"};
					elasticSearchQueries.add(new MultiMatchQuery(searchString, fields, MultiMatchQueryBuilder.Type.PHRASE_PREFIX));					
					break;
				case TITLE:				
					fields = new String[] {"title", "title.folded", "shortTitle", "shortTitle.folded", "displayTitle", "displayTitle.folded", "bookTitle", "bookTitle.folded", "seriesTitle", "seriesTitle.folded", "encyclopediaTitle", "encyclopediaTitle.folded", "publicationTitle", "publicationTitle.folded"};
					elasticSearchQueries.add(new MultiMatchQuery(searchString, fields, MultiMatchQueryBuilder.Type.PHRASE_PREFIX));
					break;
				case KEYWORD:
					fields = new String[] {"title", "title.folded", "shortTitle", "shortTitle.folded", "displayTitle", "displayTitle.folded", "date", "date.folded", "author", "author.folded", "shortTitle", "shortTitle.folded", "bookTitle", "bookTitle.folded", "seriesTitle", "seriesTitle.folded", "encyclopediaTitle", "encyclopediaTitle.folded", "publicationTitle", "publicationTitle.folded", "itemTypeDisplay", "itemTypeDisplay.folded", "place", "place.folded", "publisher", "publisher.folded", "series", "series.folded", "tags.tag", "tags.folded"};										
					String[] searchStringTokens = StringUtils.split(searchString);
					// if they entered more than one term in the keyword field, then we do a "should" operation, meaning one of the search terms should appear in a field, (a "must" would require every search term to be in a single field)
					// if they only entered one search term, then we do a "must" operation because the search term must appear in a field, otherwise it's not filtering based on the search term they entered at all
					// because it doesn't necessarily have to match any fields to be considered a match
					BoolOperation boolOperation = searchStringTokens.length > 1 ? BoolOperation.SHOULD : BoolOperation.MUST;
					elasticSearchQueries.add(new MultiMatchQuery(searchString, fields, MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, boolOperation));					
					break;
			}
			
			searchResponse = elasticSearchService.multiFieldSearch(
				IndexedType.REFERENCE_ITEM, 
				elasticSearchQueries, 
				true,
				pageable);			
		}
		
		return getReferenceSearchResult(searchResponse, pageable);
	}
	
	private ReferenceSearchResult getReferenceSearchResult(SearchResponse searchResponse, Pageable pageable) {
		ReferenceSearchResult searchResult = new ReferenceSearchResult();
		
		if(searchResponse != null) {
			SearchHit[] hits = searchResponse.getHits().getHits();
			List<ReferenceListItem> items = new ArrayList<ReferenceListItem>();
			
	        for(SearchHit hit : hits) {
	        	items.add(getReferenceListItemFromSearchHit(hit));
	        }		
			
	        searchResult.setItems(items);
	        
			if(pageable.getOffset() > 0) {
				searchResult.setPageNumber(pageable.getOffset()/pageable.getPageSize());
			}
			
			searchResult.setPageSize(pageable.getPageSize());
			searchResult.setTotalElements(searchResponse.getHits().getTotalHits());
		}		
        
		return searchResult;
	}
	
	@SuppressWarnings("unchecked")
	private ReferenceListItem getReferenceListItemFromSearchHit(SearchHit hit) {
		ReferenceListItem referenceItem = new ReferenceListItem();
		Map<String, Object> source = hit.getSource();		        	
    	Field[] fields = ReferenceListItem.class.getDeclaredFields();
    	
    	for(Field field : fields) {
    		String fieldName = field.getName();        		
    		try {
        		if("highlightFields".equals(fieldName)) {
            		referenceItem.setHighlightFieldsForDisplay(hit);
            	} else if("creators".equals(fieldName)) {
            		List<Creator> creators = new ArrayList<Creator>();
            		List<HashMap<String, Object>> creatorsMaps = (List<HashMap<String, Object>>)source.get(fieldName);
            		
            		for(HashMap<String, Object> creatorMap : creatorsMaps) {
            			Creator creator = new Creator();
            			creator.setId(Long.valueOf((Integer)creatorMap.get("id")));
            			creator.setName((String)creatorMap.get("name"));
            			creator.setFirstName((String)creatorMap.get("firstName"));
            			creator.setLastName((String)creatorMap.get("lastName"));
            			creators.add(creator);
            		}
            		
            		referenceItem.setCreators(creators);	            		
            	}  else if("tags".equals(fieldName)) { 
            		List<Tag> tags = new ArrayList<Tag>();
            		List<String> tagsList = (List<String>)source.get(fieldName);
            		
            		for(String tag : tagsList) {            			
            			tags.add(new Tag(tag));
            		}
            		
            		referenceItem.setTags(tags);
            	} else if("itemType".equals(fieldName)) {
            		ReferenceEntry.Type itemType = ReferenceEntry.Type.valueOf((String)source.get(fieldName));
            		referenceItem.setItemType(itemType);
    			} else {
    				BeanUtils.setProperty(referenceItem, fieldName, source.get(fieldName));
            	}
    		} catch (Exception e) {
    			LOG.error("Unable to set property " + fieldName + " from ElasticSearch hit to " + source.get(fieldName) + ", property will be null", e);
			}
    	}
    	
    	return referenceItem;
	}
	
	public ReferenceListItem findListItemById(Long referenceEntryId) {		
		return referenceListRepository.findOne(referenceEntryId);		
	}
	
	public ReferenceEntry saveEntry(ReferenceEntry referenceEntry) {
		return referenceEntryRepository.save(referenceEntry);
	}
	
	public ReferenceEntry getEntry(Long id) {
		return referenceEntryRepository.findOne(id);
	}	
	
	public List<ZoteroReferenceGroup> getReferenceGroupsForUser(Integer userId, String authKey) throws IOException {
		List<ZoteroReferenceGroup> groups = new ArrayList<ZoteroReferenceGroup>();
		HttpClient httpClient = remoteServiceUtil.getHttpClient();		
		GetMethod get = new GetMethod("https://api.zotero.org/users/" + userId + "/groups/items");	
		get.setRequestHeader("Authorization", "Bearer " + authKey);
		
		httpClient.executeMethod(get);
		
		if (get.getStatusCode() == HttpStatus.SC_OK) {
			String groupsJson = get.getResponseBodyAsString();			
			groups = objectMapper.readValue(groupsJson, new TypeReference<List<ZoteroReferenceGroup>>(){});
			
			for(ZoteroReferenceGroup group : groups) {
				group.setCollections(getGroupCollections(group.getId(), authKey));
			}
		}	
		
		return groups;
	}
	
	public List<ZoteroReferenceCollection> getGroupCollections(Integer groupId, String authKey) throws IOException {
		List<ZoteroReferenceCollection> collections = new ArrayList<ZoteroReferenceCollection>();
		HttpClient httpClient = remoteServiceUtil.getHttpClient();
		GetMethod get = new GetMethod("https://api.zotero.org/groups/" + groupId + "/collections");	
		get.setRequestHeader("Authorization", "Bearer " + authKey);		
		httpClient.executeMethod(get);
		
		if (get.getStatusCode() == HttpStatus.SC_OK) {
			String collectionsJson = get.getResponseBodyAsString();
			collections = objectMapper.readValue(collectionsJson, new TypeReference<List<ZoteroReferenceCollection>>(){});
			
			for(ZoteroReferenceCollection collection : collections) {
				Integer importedCount = referenceEntryRepository.countByZoteroGroupIdAndZoteroCollectionKey(groupId, collection.getKey());
				collection.setImportedItemCount(importedCount);
			}
		}
		
		return collections;
	}
	
	private ZoteroReferenceItem getZoteroItem(Integer groupId, String itemKey, String authKey) throws HttpException, IOException, FeedException {
		ZoteroReferenceItem item = null;
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + authKey);		
		String responseJson = remoteServiceUtil.executeGet("https://api.zotero.org/groups/" + groupId + "/items/" + itemKey + "?key=" + authKey, headers, true);
		JsonNode root = objectMapper.readTree(responseJson);
		JsonNode itemJson = root.get("data");
		item = objectMapper.readValue(itemJson.toString(), ZoteroReferenceItem.class);
		
		return item;
	}
	
	@SuppressWarnings("unchecked")
	private ZoteroSearchResult getCollectionItems(Integer groupId, String collectionKey, String authKey, Integer start, Integer limit, String sortBy, String sortDirection) throws HttpException, IOException, FeedException {
		ZoteroSearchResult searchResult = new ZoteroSearchResult();
		List<ZoteroReferenceItem> collectionItems = new ArrayList<ZoteroReferenceItem>();
		HttpClient httpClient = remoteServiceUtil.getHttpClient();	
		String url = "https://api.zotero.org/api/groups/" + groupId + "/collections/" + collectionKey + "/items?start=" + start + "&limit=" + limit + "&content=json";		
		
		if(StringUtils.isNotEmpty(sortBy)) {
			url += "&sort=" + sortBy + "&direction=" + (StringUtils.isNotEmpty(sortDirection) ? sortDirection : "asc");
		}
		
		GetMethod get = new GetMethod(url);
		get.setRequestHeader("Authorization", "Bearer " + authKey);
		
		httpClient.executeMethod(get);
		
		if (get.getStatusCode() == HttpStatus.SC_OK) {
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new InputStreamReader(get.getResponseBodyAsStream()));
			List<SyndEntryImpl> entries = feed.getEntries();
			Header totalResultsHeader = get.getResponseHeader("Total-Results");
			Long totalElements = totalResultsHeader != null ? Long.valueOf(totalResultsHeader.getValue()) : entries.size();
			
			searchResult.setTotalElements(totalElements);
			searchResult.setPageSize(limit);
			searchResult.setPageNumber(start/limit);			
			
			Map<ZoteroReferenceItem, ZoteroReferenceItem> existingEntries = getImportedZoteroItems();
			
			for(SyndEntryImpl entry : entries) {
				List<SyndContentImpl> contentEntries = (List<SyndContentImpl>)entry.getContents();
				
				for(SyndContentImpl content : contentEntries) {
					ZoteroReferenceItem item = objectMapper.readValue(content.getValue(), ZoteroReferenceItem.class);
					item.setImported(existingEntries.get(item) != null);
					collectionItems.add(item);						
				}				
			}
			
			searchResult.setZoteroItems(collectionItems);
		} else {
			throw new RuntimeException("Unexpected failure attempting to get Zotero collection items from Zotero web service");
		}
		
		return searchResult;
	}
	
	public ZoteroSearchResult getCollectionItems(Integer groupId, String collectionKey, String authKey, Pageable pageable) throws HttpException, IOException, FeedException {
		String sortBy = "title";
		String sortDir = "asc";
		
		if(pageable.getSort() != null && pageable.getSort().iterator().hasNext()) {
			Sort.Order sortField = pageable.getSort().iterator().next();
			sortBy = sortField.getProperty();
			sortDir = sortField.getDirection().equals(Direction.ASC) ? "asc" : "desc";
		}
		
		return getCollectionItems(groupId, collectionKey, authKey, pageable.getOffset(), pageable.getPageSize(), sortBy, sortDir);
	}
	
	@Transactional
	public ZoteroCollectionImportResult importZoteroCollectionEntries(Integer groupId, String collectionKey, String authKey) throws HttpException, IOException, FeedException {		
		ZoteroCollectionImportResult importResult = new ZoteroCollectionImportResult();
		
		try {
			ZoteroSearchResult searchResult = getAllCollectionEntriesForImport(groupId, collectionKey, authKey);
			List<ReferenceEntry> entries = searchResult.getReferenceEntries();
			setEntryCreators(entries);
			setEntryTags(entries);
			// we do this at the end rather than filtering the list right away because the call that gets 
			// the existing entries is a select-for-update that will lock the rows to ensure data integrity,
			// that blocking call is expensive so we defer it until the very end
			List<ReferenceEntry> entriesToImport = filterOutExistingEntriesForImport(entries);
			referenceEntryRepository.save(entriesToImport);
			Integer alreadyImportedCount = entries.size() - entriesToImport.size();
			// this includes both previously imported items and the newly imported items
			elasticSearchService.reindex(IndexedType.REFERENCE_ITEM, findAll());
			importResult.setCollectionItems(searchResult.getZoteroItems());
			importResult.setMessage("Import completed successfully! " + entriesToImport.size() + " of the total " + searchResult.getTotalElements() + " citation" + (searchResult.getTotalElements() == 1 ? "" : "s") + " in the collection were imported. " + searchResult.getSkippedItems() + " citation" + (searchResult.getSkippedItems() == 1 ? " was" : "s were") + " skipped (notes and attachments are skipped) and " + alreadyImportedCount + " had already been imported.");
			importResult.setSuccess(true);
		} catch(Exception e) {
			String message = "Exception attempting to import reference items from Zotero for group ID: " + groupId + " and collection key:" + collectionKey;
			LOG.error(message, e);
			importResult.setMessage("An unexpected error occurred while trying to run the import. Please check the server logs for details or contact the system adminstrator for assistance.");
		}		
		
		return importResult;
	}	
	
	@Transactional
	public ZoteroItemImportResult importZoteroItem(Integer groupId, String itemKey, String authKey) throws HttpException, IOException, FeedException {
		ZoteroItemImportResult importResult = new ZoteroItemImportResult();
		
		try {			
			ZoteroReferenceItem zoteroItem = getZoteroItem(groupId, itemKey, authKey);
			// just set the collection key to the first collection the item belongs to, we only use the collection key for
			// bulk delete of items by group id & collection key
			String collectionKey = CollectionUtils.isEmpty(zoteroItem.getCollections()) ? null : zoteroItem.getCollections().get(0); 
			ReferenceEntry entry = new ReferenceEntry(zoteroItem, groupId, collectionKey);
			setEntryCreators(Arrays.asList(entry));
			setEntryTags(Arrays.asList(entry));
			referenceEntryRepository.save(entry);
			ReferenceListItem listItemForIndex = findListItemById(entry.getId());
			elasticSearchService.updateIndexedObject(IndexedType.REFERENCE_ITEM, entry.getId().toString(), objectMapper.writeValueAsString(listItemForIndex));
			importResult.setItem(zoteroItem);
			importResult.setSuccess(true);
		} catch(Exception e) {
			String message = "Exception attempting to import reference item from Zotero for group ID: " + groupId + " and item key:" + itemKey;
			LOG.error(message, e);
			importResult.setMessage("An unexpected error occurred while trying to run the import. Please check the server logs for details or contact the system adminstrator for assistance.");
		}		
		
		return importResult;
	}
	
	@Transactional
	public void deleteEntry(Long entryId) {
		referenceEntryRepository.delete(entryId);
		elasticSearchService.delete(IndexedType.REFERENCE_ITEM, entryId.toString());
	}
	
	@Transactional
	public void deleteByZoteroCollection(Integer zoteroGroupId, String zoteroCollectionKey) {
		referenceEntryRepository.deleteByZoteroGroupIdAndZoteroCollectionKey(zoteroGroupId, zoteroCollectionKey);		
	}
	
	public void sendEntryToUser(Long entryId, String emailAddress) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		ReferenceEntry entry = referenceEntryRepository.findOne(entryId);
		
		if(entry != null) {
			String message = "Dear vHMML user,<br/><br/>";
			message += "Here is the citation reference information you emailed to yourself from the vHMML website:<br/></br>";
			List<DisplayField> fields = entry.getDisplayFields();
			
			for(DisplayField field : fields) {
				message += field.getLabel() + ": " + BeanUtils.getProperty(entry, field.getName()) + "<br/>";
			}
			
			message += "<br/>" + configService.getValue(Property.VHMML_EMAIL_SIGNATURE);
			
			emailService.sendMessages(Lists.newArrayList(emailAddress), "vHMML Citation Reference", message);
		}			
	}
	
	@Transactional
	private ZoteroSearchResult getAllCollectionEntriesForImport(Integer groupId, String collectionKey, String authKey) throws HttpException, IOException, FeedException {
		ZoteroSearchResult allEntriesSearchResult = new ZoteroSearchResult();
		ZoteroSearchResult searchResult = null;
		List<ReferenceEntry> entries = new ArrayList<ReferenceEntry>();
		int skippedItems = 0;
		
		while(searchResult == null || (entries.size() + skippedItems) < searchResult.getTotalElements()) {
			
			searchResult = getCollectionItems(groupId, collectionKey, authKey, entries.size() + skippedItems, 50, null, null);
			List<ZoteroReferenceItem> items = searchResult.getZoteroItems();
			allEntriesSearchResult.getZoteroItems().addAll(items);
			
			for(ZoteroReferenceItem item : items) {
				String itemType = item.getItemType();

				if(!"note".equalsIgnoreCase(itemType) && !"attachment".equalsIgnoreCase(itemType)) {
					item.setImported(true);
					entries.add(new ReferenceEntry(item, groupId, collectionKey));
				} else {
					skippedItems++;
				}
			}			
		}
		
		allEntriesSearchResult.setTotalElements(searchResult.getTotalElements());
		allEntriesSearchResult.setReferenceEntries(entries);
		allEntriesSearchResult.setSkippedItems(skippedItems);
		
		return allEntriesSearchResult;
	}
	
	@Transactional
	private void setEntryCreators(List<ReferenceEntry> entries) {
		// get list of unique creators between both the new entries and the database so we don't re-insert existing ones or insert duplicates
		Map<Creator, Creator> uniqueCreators = getUniqueCreators(entries);
		
		// now set the saved creator on the entries so they reference the existing ones instead of creating new ones
		for(ReferenceEntry entry : entries) {
			if(!CollectionUtils.isEmpty(entry.getReferenceCreators())) {
				
				for(ReferenceCreator libCreator : entry.getReferenceCreators()) {
					Creator savedCreator = uniqueCreators.get(libCreator.getCreator());
					libCreator.setCreator(savedCreator);
				}				
			}
		}
	}
	
	@Transactional
	private Map<Creator, Creator> getUniqueCreators(List<ReferenceEntry> entries) {
		// flatten out the list of unique creators so we only have one record for each
		Map<Creator, Creator> uniqueCreators = new HashMap<Creator, Creator>();
		
		// get list of existing creators in the database so we don't re-import existing ones
		Map<Creator, Creator> existingCreators = getCreatorsForUpdate();
		
		for(ReferenceEntry entry : entries) {
			
			if(!CollectionUtils.isEmpty(entry.getReferenceCreators())) {
				for(ReferenceCreator libCreator : entry.getReferenceCreators()) {
					Creator creator = libCreator.getCreator();
					
					if(!uniqueCreators.containsKey(creator)) {
						// if the creator already exists in the database, put that one in the list so it won't
						// be inserted again, any entries that reference it will reference the existing one
						Creator existingCreator = existingCreators.get(creator);						
						creator = existingCreator == null ? creator : existingCreator; 
						uniqueCreators.put(creator, creator);
					}
				}
			}					
		}
		
		return uniqueCreators;
	}
	
	@Transactional
	private Map<Creator, Creator> getCreatorsForUpdate() {
		Map<Creator, Creator> creators = new HashMap<Creator, Creator>();
		
		List<Creator> creatorList = creatorRepository.findAllForUpdate();
		
		for(Creator creator : creatorList) {
			creators.put(creator, creator);
		}
		
		return creators;
	}
	
	@Transactional
	private void setEntryTags(List<ReferenceEntry> entries) {
		// get list of unique tags between both the new entries and the database so we don't re-insert existing ones or insert duplicates
		Map<Tag, Tag> uniqueTags = getUniqueTags(entries);
		
		// now set the saved creator on the entries so they reference the existing ones instead of creating new ones
		for(ReferenceEntry entry : entries) {
			if(!CollectionUtils.isEmpty(entry.getReferenceTags())) {
				
				for(ReferenceTag refTag : entry.getReferenceTags()) {
					Tag savedTag = uniqueTags.get(refTag.getTag());
					refTag.setTag(savedTag);
				}				
			}
		}
	}
	
	@Transactional
	private Map<Tag, Tag> getUniqueTags(List<ReferenceEntry> entries) {
		// flatten out the list of unique creators so we only have one record for each
		Map<Tag, Tag> uniqueTags = new HashMap<Tag, Tag>();
		
		// get list of existing creators in the database so we don't re-import existing ones
		Map<Tag, Tag> existingTags = getTagsForUpdate();
		
		for(ReferenceEntry entry : entries) {
			
			if(!CollectionUtils.isEmpty(entry.getReferenceTags())) {
				for(ReferenceTag refTag : entry.getReferenceTags()) {
					Tag tag = refTag.getTag();
					
					if(!uniqueTags.containsKey(tag)) {
						// if the tag already exists in the database, put that one in the list so it won't
						// be inserted again, any entries that reference it will reference the existing one
						Tag existingTag = existingTags.get(tag);						
						tag = existingTag == null ? tag : existingTag; 
						uniqueTags.put(tag, tag);
					}
				}
			}					
		}
		
		return uniqueTags;
	}
	
	@Transactional
	private Map<Tag, Tag> getTagsForUpdate() {
		Map<Tag, Tag> tags = new HashMap<Tag, Tag>();
		
		List<Tag> tagList = tagRepository.findAllForUpdate();
		
		for(Tag tag : tagList) {
			tags.put(tag, tag);
		}
		
		return tags;
	}
	
	private Map<ZoteroReferenceItem, ZoteroReferenceItem> getImportedZoteroItems() {
		Map<ZoteroReferenceItem, ZoteroReferenceItem> importedItems = new HashMap<ZoteroReferenceItem, ZoteroReferenceItem>(); 
		Iterable<ReferenceEntry> entriesList = referenceEntryRepository.findAll();
		
		for(ReferenceEntry entry : entriesList) {
			ZoteroReferenceItem item = new ZoteroReferenceItem(entry);
			importedItems.put(item, item);
		}
		
		return importedItems;
	}
	
	@Transactional
	private List<ReferenceEntry> filterOutExistingEntriesForImport(List<ReferenceEntry> entries) {
		List<ReferenceEntry> entriesToImport = new ArrayList<ReferenceEntry>();
		Map<ReferenceEntry, ReferenceEntry> existingEntries = new HashMap<ReferenceEntry, ReferenceEntry>();
		List<ReferenceEntry> entriesList = referenceEntryRepository.findAllForUpdate();
		
		for(ReferenceEntry entry : entriesList) {
			existingEntries.put(entry, entry);			
		}
		
		for(ReferenceEntry entry : entries) {
			if(existingEntries.get(entry) == null) {
				entriesToImport.add(entry);
			}
		}
		
		return entriesToImport;
	}
}
