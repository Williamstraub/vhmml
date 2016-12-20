package org.vhmml.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vhmml.dto.ReadingRoomSearchHit;
import org.vhmml.dto.ReadingRoomSearchResult;
import org.vhmml.dto.elasticsearch.BoolMultiMatchQuery;
import org.vhmml.dto.elasticsearch.BoolQuery;
import org.vhmml.dto.elasticsearch.DateRangeFilter;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery;
import org.vhmml.dto.elasticsearch.ElasticSearchQuery.BoolOperation;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.dto.elasticsearch.MatchQuery;
import org.vhmml.dto.elasticsearch.MultiMatchQuery;
import org.vhmml.dto.elasticsearch.MultipleFieldRangeQuery;
import org.vhmml.dto.elasticsearch.TermQuery;
import org.vhmml.dto.elasticsearch.TermsQuery;
import org.vhmml.dto.elasticsearch.WildcardQuery;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalContent;
import org.vhmml.entity.readingroom.ArchivalContributor;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalDataLanguage;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.ContentLanguage;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.entity.readingroom.Image;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObject.AccessRestriction;
import org.vhmml.entity.readingroom.ReadingRoomObject.Type;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.entity.readingroom.ReadingRoomObjectFeature;
import org.vhmml.entity.readingroom.ReadingRoomObjectGenre;
import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.ReadingRoomObjectSubject;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.entity.readingroom.Support;
import org.vhmml.exception.ObjectLockedException;
import org.vhmml.repository.readingroom.ArchivalContributorRepository;
import org.vhmml.repository.readingroom.ArchivalDataLanguageRepository;
import org.vhmml.repository.readingroom.ArchivalDataRepository;
import org.vhmml.repository.readingroom.ReadingRoomAlternateSurrogateRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentLanguageRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentRepository;
import org.vhmml.repository.readingroom.ReadingRoomContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomImageRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectBibUrlRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectExtentRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectFacsimileUrlRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectFeatureRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectGenreRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectSubjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomOverviewRepository;
import org.vhmml.repository.readingroom.ReadingRoomPartContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomPartRepository;
import org.vhmml.repository.readingroom.ReadingRoomSubjectRepository;
import org.vhmml.repository.readingroom.UniformTitleRepository;
import org.vhmml.security.VhmmlSecurityUtil;
import org.vhmml.util.AttributeUtil;
import org.vhmml.util.ElasticSearchUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.ibm.icu.util.Calendar;

@Service
public class ReadingRoomServiceImpl implements ReadingRoomService {	
	
	private static final Logger LOG = Logger.getLogger(ReadingRoomServiceImpl.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	// This enum represents a search field in vHMML, one field may actually search across multiple fields in Elastic Search.
	// The group name is used to group related fields. For example, when the user searches the "Title" field, they are actually 
	// searching across title, alternate title and uniform title. Each individual field has it's own display name so search 
	// results can report which specific field actually matched a user's search. For example, if they searched for "Book of Hours"
	// in the Title field, the search result would show that "Book of Hours" was found in the Uniform Title field.	
	protected enum SearchField {	
		SHELFMARK("Shelfmark", "shelfmark", new String[]{"shelfmark"}),
		TITLE("Title", "title", new String[]{"titles"}),
		TITLE_NS("Title Native Script", "title", new String[]{"titlesNs"}),
		ALTERNATE_TITLE("Alternate Title", "title", new String[]{"alternateTitles"}),
		UNIFORM_TITLE("Uniform Title", "title", new String[]{"uniformTitles"}),		
		HMML_PROJECT_NUMBER("HMML Project Number", "hmmlProjectNumber", new String[]{"hmmlProjectNumber", "hmmlProjectNumber.zero_trim", "hmmlProjectNumber.zero_trim_folded"}, false),
		ALTERNATE_SURROGATES("Alternate Surrogates", "hmmlProjectNumber", new String[]{"alternateSurrogates", "alternateSurrogates.zero_trim", "alternateSurrogates.zero_trim_folded"}),
		PROVENANCE("Provenance", "shelfmark", new String[]{"provenance"}),
		BINDING("Binding", "binding", new String[]{"binding"}),
		COUNTRY("Country", "country", new String[]{"country"}),
		CITY("City", "city", new String[]{"city"}),
		HOLDING_INSTITUTION("Holding Institution", "holdingInstitution", new String[]{"holdingInstitution"}),
		REPOSITORY("Repository", "repository", new String[]{"repository"}),
		CENTURIES("Centuries", "centuries", new String[]{"centuries"}),
		SUPPORT("Support", "support", new String[]{"support"}),
		LANGUAGES("Languages", "language", new String[]{"languages"}),
		AUTHOR("Author", "author", new String[]{"authors"}),
		AUTHOR_NS("Author Native Script", "author", new String[]{"authorsNs"}),
		AUTHOR_DISPLAY("Author Display Name", "author", new String[]{"authorsDisplay"}),
		CONTRIBUTORS("Associated Names", "author", new String[]{"otherContributors"}),
		CONTRIBUTORS_DISPLAY("Associated Names Display", "author", new String[]{"otherContributorsDisplay"}),		
		PART_CONTRIBUTORS("Associated Names", "author", new String[]{"partOtherContributors"}),
		PART_CONTRIBUTORS_DISPLAY("Associated Names Display", "author", new String[]{"partOtherContributorsDisplay"}),		
		ARCHIVAL_CONTRIBUTORS("Associated Names", "author", new String[]{"archivalContributors"}),
		ARCHIVAL_CONTRIBUTORS_DISPLAY("Associated Names Display", "author", new String[]{"archivalContributorsDisplay"}),
		ARCHIVAL_CREATOR("Creator", "author", new String[]{"archivalCreators"}),
		ARCHIVAL_TITLE("Title", "title", new String[]{"archivalTitle"}),
		ARCHIVAL_CONTENT_DESCRIPTION("Description", new String[] {"author", "title"}, new String[]{"archivalContentDescription"}),
		ARCHIVAL_CONTENT_NOTES("Notes", "notes", new String[]{"archivalContentNotes"}),
		ARCHIVAL_BIBLIOGRAPHY("Bibliography", "bibliography", new String[]{"archivalBibliography"}),
		ARCHIVAL_SCOPE_CONTENT("Scope and Contents", "archivalScopeContent", new String[]{"archivalScopeContent"}),
		ARCHIVAL_CUSTODIAL_HISTORY("Custodial History", "archivalCustodialHistory", new String[]{"archivalCustodialHistory"}),
		ARCHIVAL_HISTORICAL_NOTE("Historical Note", "archivalHistoricalNote", new String[]{"archivalHistoricalNote"}),		
		ARTISTS("Artist", "author", new String[]{"artists"}),
		SCRIBES("Scribe", "author", new String[]{"scribes"}),
		SCRIBES_NS("Scribe Native Script", "author", new String[]{"scribesNs"}),
		FORMER_OWNERS("Former Owner", "author", new String[]{"formerOwners"}),
		SUBJECT("Subject", "subject", new String[]{"subjects"}),
		GENRE("Genre", "genres", new String[]{"genres"}),
		CENTURY("Century", "century", new String[]{"centuriesDisplay", "centuryList"}),
		INCIPIT("Incipit", "incipit", new String[]{"incipit"}),
		FEATURES("Features", "features", new String[]{"features"}),
		SCRIPTS("Scripts", "script", new String[]{"scripts"}),
		WRITING_SYSTEM("Writing System", "writingSystem", new String[]{"writingSystems"}),		
		PLACE_OF_ORIGIN("Place Of Origin", "placeOfOrigin", new String[]{"placeOfOrigin"}),		
		COMMON_NAME("Common Name", "commonName", new String[]{"commonName"}),		
		NOTES("Notes", "notes", new String[]{"notes"}),		
		BIBLIOGRAPHY("Bibliography", "bibliography", new String[]{"bibliography"}),	
		DECORATION("Decoration/Illustration", "decoration", new String[]{"decoration"});		
		
		private String displayName;
		private String[] fieldGroups;
		private String[] fields;
		private boolean keyword;
		
		SearchField(String displayName, String fieldGroup, String[] fields) {
			this(displayName, new String[] {fieldGroup}, fields);
		}
		
		SearchField(String displayName, String[] fieldGroups, String[] fields) {
			this(displayName, fieldGroups, fields, true);
		}
		
		SearchField(String displayName, String fieldGroup, String[] fields, boolean keyword) {
			this(displayName, new String[] {fieldGroup}, fields, keyword);
		}
		
		SearchField(String displayName, String[] fieldGroups, String[] fields, boolean keyword) {
			this.displayName = displayName;
			this.fieldGroups = fieldGroups;
			this.fields = fields;
			this.keyword = keyword;
		}
		
		public static String[] getAllFields() {
			List<String> allFields = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				allFields.addAll(field.getIndexedFields());
			}
			
			return allFields.toArray(new String[allFields.size()]);
		}
		
		public static String[] getKeywordFields() {
			List<String> allFields = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				if(field.isKeyword()) {
					allFields.addAll(field.getIndexedFields());
				}				
			}
			
			return allFields.toArray(new String[allFields.size()]);
		}
		
		public static String[] getKeywordExactMatchFields() {
			List<String> exactMatchFields = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				if(field.isKeyword()) {
					exactMatchFields.addAll(field.getBasicFields());
				}				
			}
			
			return exactMatchFields.toArray(new String[exactMatchFields.size()]);
		}
		
		public static String[] getFieldsForGroup(String fieldGroupName) {
			List<String> fieldsForGroup = new ArrayList<>();
			
			for(SearchField field : SearchField.values()) {
				String[] groupsForField = field.getFieldGroups();
				
				for(String fieldGroup : groupsForField) {
					if(fieldGroup.equals(fieldGroupName)) {
						fieldsForGroup.addAll(field.getIndexedFields());
					}
				}								
			}
			
			return fieldsForGroup.toArray(new String[fieldsForGroup.size()]);
		}
		
		public static Map<String, String> getDisplayNameMap() {
			Map<String, String> displayNameMap = new HashMap<>();
			
			for(SearchField field : SearchField.values()) {
				for(String fieldName : field.getFields()) {
					displayNameMap.put(fieldName, field.getDisplayName());
				}
			}
			
			return displayNameMap;
		}

		public String getDisplayName() {
			return displayName;
		}

		public String[] getFieldGroups() {
			return fieldGroups;
		}

		public String[] getFields() {
			return fields;
		}
		
		// indexed fields contains all versions of the field that are indexed, i.e. title, title.folded, etc. 
		public List<String> getIndexedFields() {
			List<String> indexedFields = new ArrayList<>();
			
			for(String field : fields) {
				indexedFields.add(field);
				indexedFields.add(field + ".folded");
			}
			
			return indexedFields;
		}
		
		// basic fields are case insensitive & diacritic insensitive only, no stemming, stop words, etc.
		// they are used for exact phrase search when the user surrounds the search text with quotes
		public List<String> getBasicFields() {
			List<String> indexedFields = new ArrayList<>();
			
			for(String field : fields) {
				indexedFields.add(field + ".basic");
			}
			
			return indexedFields;
		}

		public boolean isKeyword() {
			return keyword;
		}
	}
	
	@Autowired
	private ReadingRoomOverviewRepository readingRoomOverviewRepository;
	
	@Autowired
	private ReadingRoomObjectRepository readingRoomObjectRepository;
	
	@Autowired
	private ReadingRoomPartRepository readingRoomPartRepository;
	
	@Autowired
	private ArchivalDataRepository archivalDataRepository;
	
	@Autowired
	private ArchivalContributorRepository archivalContributorRepository;
	
	@Autowired
	private ArchivalDataLanguageRepository archivalDataLanguageRepository;
	
	@Autowired
	private ReadingRoomPartContributorRepository readingRoomPartContributorRepository;
	
	@Autowired
	private ReadingRoomContentRepository readingRoomContentRepository;
	
	@Autowired
	private ReadingRoomImageRepository readingRoomImageRepository;		
	
	@Autowired
	private ReadingRoomSubjectRepository subjectRepository;
	
	@Autowired
	private ReadingRoomContentLanguageRepository readingRoomContentLangRepository;
	
	@Autowired
	private ReadingRoomObjectSubjectRepository readingRoomObjectSubjectRepository;	
	
	@Autowired
	private ReadingRoomAlternateSurrogateRepository alternateSurrogateRepository;
	
	@Autowired
	private ReadingRoomObjectGenreRepository readingRoomObjectGenreRepository;
	
	@Autowired
	private ReadingRoomObjectFeatureRepository readingRoomObjectFeatureRepository;
	
	@Autowired
	private ReadingRoomObjectBibUrlRepository readingRoomObjectBibUrlRepository;
	
	@Autowired
	private ReadingRoomObjectFacsimileUrlRepository readingRoomObjectFacsimileUrlRepository;
	
	@Autowired
	private ReadingRoomObjectExtentRepository readingRoomObjectExtentRepository;
	
	@Autowired
	private ReadingRoomObjectContributorRepository readingRoomObjectContributorRepository;
	
	@Autowired
	private ReadingRoomContributorRepository readingRoomContributorRepository;
	
	@Autowired
	private ReadingRoomContentContributorRepository contentContributorRepository;
	
	@Autowired
	private ReadingRoomObjectExtentRepository extentRepository;	
	
	@Autowired
	private UniformTitleRepository uniformTitleRepository;		
	
	@Autowired
	private AuthorityListService authorityListService;
	
	@Autowired
	private ElasticSearchService elasticSearchService;	
	
	public ReadingRoomServiceImpl() {
		super();
	}
	
	public List<ReadingRoomObject> getAllObjects() {
		return Lists.newArrayList(readingRoomObjectRepository.findAll());
	}
	
	public List<ReadingRoomObjectOverview> getAllOverviews() {
		return readingRoomOverviewRepository.findAllObjectOverviews();
	}	
	
	public ReadingRoomObject findById(Long id) {
		ReadingRoomObject object = readingRoomObjectRepository.findOne(id);		
		setTransientAttributes(object);
		
		return object;
	}
	
	public List<ReadingRoomObject> findByCollection(String collectionName) {
		return readingRoomObjectRepository.findByHmmlProjectNumberStartsWith(collectionName);
	}
	
	public ReadingRoomObject findByHmmlProject(String hmmlProjectNumber) {
		ReadingRoomObject object = readingRoomObjectRepository.findByHmmlProjectNumber(hmmlProjectNumber);
		setTransientAttributes(object);
		
		return object;
	}
	
	private void setTransientAttributes(ReadingRoomObject object) {
		object.setEditable(currentUserCanEdit(object));
		
		if(object.getType() == Type.ARCHIVAL_OBJECT) {
			ArchivalData archivalData = ((ArchivalObject)object).getArchivalData();
			
			if(archivalData != null) {
				archivalData.setWritingSystemList(AttributeUtil.getStringAttributeAsList(archivalData.getWritingSystem()));
				
				List<ArchivalContent> contentList = archivalData.getContent();
				
				if(archivalData != null && CollectionUtils.isNotEmpty(contentList)) {
					archivalData.setHasArchivalContent(true);
					
					for(ArchivalContent content : contentList) {
						content.setScribes(content.getContributorsByType(Contributor.Type.SCRIBE));
						content.setArtists(content.getContributorsByType(Contributor.Type.ARTIST));
						content.setAssociatedNames(content.getContributorsExcludingTypes(Contributor.Type.SCRIBE, Contributor.Type.ARTIST));
						content.setWritingSystemList(AttributeUtil.getStringAttributeAsList(content.getWritingSystem()));
					}
				}
			}			
		}
	}
	
	@Transactional
	public ReadingRoomObject saveReadingRoomObject(ReadingRoomObject object) throws IOException {		
		
		Long objectId = object.getId();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUser = user.getUsername();
		
		// user can create new subjects & contributors on the fly, if they enter a new name, it's new
		// so we have to look in the database to see if the subjects & contributors set on the object exist,
		// if so we use the ones from the database, if not we need to insert them
		object.setSubjects(authorityListService.findOrCreateNamedEntities(object.getSubjects(), subjectRepository));
		object.setContributors(findOrCreateContributors(object.getContributors()));
		
		if(CollectionUtils.isNotEmpty(object.getParts())) {
			for(ReadingRoomObjectPart part : object.getParts()) {
				
				if(CollectionUtils.isNotEmpty(part.getContributors())) {
					part.setContributors(findOrCreatePartContributors(part.getContributors()));					
				}
				
				if(CollectionUtils.isNotEmpty(part.getContents())) {
					for(Content content : part.getContents()) {
						content.setContentContributors(findOrCreateContentContributors(content.getContentContributors()));
						content.setUniformTitle(authorityListService.findOrCreateNamedEntity(content.getUniformTitle(), uniformTitleRepository));
					}
				}
			}
		}
		
		findOrCreateArchivalData(object);
		
		Date now = new Date();
		
		if(objectId != null) {
			// updating an existing object, just remove data from join tables and insert new data
			ReadingRoomObject dbObject = readingRoomObjectRepository.findOne(objectId);
			
			for(ReadingRoomObjectPart part : dbObject.getParts()) {
				
				if(CollectionUtils.isNotEmpty(part.getContributors())) {
					readingRoomPartContributorRepository.delete(part.getContributors());
				}
				
				List<Content> partContents = part.getContents();
				
				for(Content content : partContents) {
					if(CollectionUtils.isNotEmpty(content.getImages())) {
						readingRoomImageRepository.delete(content.getImages());
					}
					
					if(CollectionUtils.isNotEmpty(content.getContentContributors())) {
						contentContributorRepository.delete(content.getContentContributors());
					}					
				}
				
				readingRoomContentRepository.delete(partContents);
			}

			readingRoomPartRepository.delete(dbObject.getParts());			
			alternateSurrogateRepository.delete(dbObject.getAlternateSurrogates());
			readingRoomObjectBibUrlRepository.delete(dbObject.getExternalBibliographyUrls());
			readingRoomObjectFacsimileUrlRepository.delete(dbObject.getExternalFacsimileUrls());
			readingRoomObjectExtentRepository.delete(dbObject.getExtents());
			readingRoomObjectContributorRepository.delete(dbObject.getContributors());
			
			if(dbObject instanceof ArchivalObject && ((ArchivalObject)dbObject).getArchivalData() != null) {
				archivalDataRepository.delete(((ArchivalObject)dbObject).getArchivalData());
			}
			
			// only admin & level 1 cataloger can change inputter or input date
			if(!user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER))) {
				object.setInputter(dbObject.getInputter());
			}
			
			// these two things never change so keep what is in the database
			object.setInputDate(dbObject.getInputDate());			
			object.setDataSource(dbObject.getDataSource());
			BeanUtils.copyProperties(object, dbObject);
			object = dbObject;		
		} else {
			object.setInputter(currentUser);
			// sadly we don't store input date as a date because it comes in as an unpredictable string on imports
			object.setInputDate(new SimpleDateFormat("yyyy/MM/dd 'at' hh:mm aaa z").format(now));
		}		
		
		// support is an attribute of part, but we store it denormalized for performance, otherwise to get a 
		// nicely rolled up list of unique supports among parts would require a supports table and a join part_supports 
		// table which would also further complicate the already expensive reading_room_object_overview view used to 
		// populate the search index
		object.setSupport(getSupportList(object.getParts()));
		object.setLastUpdate(now);
		object.setLastUpdatedBy(currentUser);
		
		readingRoomObjectRepository.save(object);				
		ReadingRoomObjectOverview overview = readingRoomOverviewRepository.findObjectOverview(object.getId());
		elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_OBJECT, object.getId().toString(), objectMapper.writeValueAsString(overview));
		trackObjectSave(object);		
		
		return object;
	}
	
	private void trackObjectSave(ReadingRoomObject object) {
		String username = VhmmlSecurityUtil.getCurrentUser().getUsername();
		String projectNumber = object.getHmmlProjectNumber();
		
		try {
			
			BoolQuery existingDataQuery = new BoolQuery();
			existingDataQuery.must(new MatchQuery("username.untouched", username));
			existingDataQuery.must(new MatchQuery("hmmlProjectNumber.untouched", projectNumber));			
			Date now = new Date();			
			GregorianCalendar start = new GregorianCalendar();
			GregorianCalendar end = new GregorianCalendar();
			start.setTime(now);
			start.add(Calendar.HOUR, -12);
			end.setTime(now);
			end.add(Calendar.HOUR, 12);			
			DateRangeFilter dateFilter = new DateRangeFilter("date", ImageService.IMAGE_VIEW_DATE_FORMAT, start.getTime(), end.getTime());
			SearchResponse dataExistsResponse = elasticSearchService.executeQuery(IndexedType.READING_ROOM_OBJECT_SAVE, existingDataQuery.getQuery(), dateFilter);
					
			if(dataExistsResponse.getHits().hits().length == 0) {
				Map<String, String> imageViewData = new HashMap<>();								
				imageViewData.put("username", username);
				imageViewData.put("hmmlProjectNumber", projectNumber);
				imageViewData.put("date", new SimpleDateFormat(ImageService.IMAGE_VIEW_DATE_FORMAT).format(new Date()));
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_OBJECT_SAVE, null, objectMapper.writeValueAsString(imageViewData));
			}			
		} catch(Exception e) {
			LOG.error("Unexpected exception attempting to track object save statistics for object " + object + ", and user " + username, e);
		}		
	}
	
	/**
	 * Updating object type is a sticky situation because we're using JPA single-table inheritance which doesn't allow
	 * you to change the value of an object's discriminator column, so we use a native query to update the type. 
	 * Because of this, changing object type as part of the save method above causes problems because of the fact that the save method is 
	 * using normal JPA queries which are cached by the JPA entity manager. So if we read the object to get it's 
	 * type, use a native query to update the type if it needs to change, then try to read the object again so we get the correct 
	 * object type back, we won't get the correct object back because the updates are in a different transaction that hasn't been committed
	 * yet (native queries happen in a different transaction). The are some hacky ways to get around this by interacting directly with
	 * the JPA entity manager but it gets pretty ugly and painful. So, if the object type is going to change, we call
	 * this method before the save method to update the type (and delete archival data if the object change from archival to non-archival). That way,
	 * the object type is already changed and can be read back as the new type in the save method.
	 * 
	 */
	@Transactional 
	public void updateObjectType(Long objectId, ReadingRoomObject.Type type) {
		ReadingRoomObject object = readingRoomObjectRepository.findOne(objectId);		
		
		if(object != null) {
			readingRoomObjectRepository.updateObjectType(type, objectId);
			
			if(object instanceof ArchivalObject) {
				// need to delete archival data manually too, otherwise it will be orphaned and reading the object
				// as it's new "non-archival" type will fail because it tries to set the archival data on a non-archival object
				archivalDataRepository.deleteByReadingRoomObjectId(objectId);
			}
		}		
	}
	
	@Transactional
	public void setLocked(Long objectId, boolean lock) throws IllegalAccessException, IOException, ObjectLockedException {
		ReadingRoomObject object = readingRoomObjectRepository.findOne(objectId);

		// object could be null if they just deleted it
		if(object != null) {
			if(!currentUserCanEdit(object)) {
				throw new IllegalAccessException("The current user is not allowed to lock/unlock reading room object with id " + objectId);			
			} else {			
				// it's locked by someone else you can't change the lock state
				if(object.isLocked() && object.getLockedByDifferentUser()) {
					User user = VhmmlSecurityUtil.getCurrentUser();
					LOG.warn("User " + user.getUsername() + " failed to " + (lock ? "acquire lock on" : "unlock") + " object " + objectId + " because it's already locked by user " + object.getLockedBy());
					throw new ObjectLockedException("Unable to lock object " + object.getHmmlProjectNumber() + " for editing because it is currently being edited by another user.");
				} else if(lock != object.isLocked()) {
					object.setLocked(lock);
					object.setLockedBy(lock ? VhmmlSecurityUtil.getCurrentUser().getUsername() : null);
					readingRoomObjectRepository.save(object);
					ReadingRoomObjectOverview overview = readingRoomOverviewRepository.findObjectOverview(object.getId());
					elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_OBJECT, object.getId().toString(), objectMapper.writeValueAsString(overview));
				}			
			}
		}	
	}
	
	// this method is called from the AppStartupListener
	@Transactional
	public void clearLockedRecords() throws IOException  {
		clearLockedRecords(readingRoomObjectRepository.findLocked());
	}
	
	@Transactional
	public void clearLockedRecordsForUser(String username) throws IOException  {
		clearLockedRecords(readingRoomObjectRepository.findLockedByUsername(username));
	}
	
	private void clearLockedRecords(List<ReadingRoomObject> lockedObjects) throws IOException {
		if(CollectionUtils.isNotEmpty(lockedObjects)) {
			for(ReadingRoomObject object : lockedObjects) {
				object.setLocked(false);
				object.setLockedBy(null);
			}
			
			readingRoomObjectRepository.save(lockedObjects);

			// re-index after we know the save has succeeded (it won't happen if an exception is thrown on save), so data & index stay in sync
			// note that update by query api isn't available until Elastic 2.3.0, so we have to update the index one document at a time 
			for(ReadingRoomObject object : lockedObjects) {
				ReadingRoomObjectOverview overview = readingRoomOverviewRepository.findObjectOverview(object.getId());
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_OBJECT, object.getId().toString(), objectMapper.writeValueAsString(overview));
			}
		}		
	}
	
	@Transactional
	public void deleteReadingRoomObject(Long readingRoomObjectId) throws IllegalAccessException {
		ReadingRoomObject object = readingRoomObjectRepository.findOne(readingRoomObjectId);
				
		if(currentUserCanEdit(object)) {
			readingRoomObjectRepository.delete(readingRoomObjectId);
			elasticSearchService.removeIndexedObject(IndexedType.READING_ROOM_OBJECT, readingRoomObjectId.toString());
		} else {
			throw new IllegalAccessException("The current user is not allowed to delete reading room object with id " + readingRoomObjectId);
		}
	}
	
	@Transactional
	public void deleteReadingRoomObjectPart(Long partId) throws IllegalAccessException, IOException {
		
		ReadingRoomObjectPart part = readingRoomPartRepository.findOne(partId);
		
		if(part != null) {
			ReadingRoomObject parentObject = part.getParentObject();
			
			if(currentUserCanEdit(parentObject)) {
				parentObject.getParts().remove(part);
				parentObject.setSupport(getSupportList(parentObject.getParts()));				
				readingRoomObjectRepository.save(parentObject);
				ReadingRoomObjectOverview overview = readingRoomOverviewRepository.findObjectOverview(parentObject.getId());
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_OBJECT, parentObject.getId().toString(), objectMapper.writeValueAsString(overview));
			} else {
				throw new IllegalAccessException("The current user is not allowed to delete reading room object with id " + parentObject.getId());
			}			
		}		
	}
	
	@Transactional
	public void deleteReadingRoomContent(Long contentItemId) throws IllegalAccessException {
		Content content = readingRoomContentRepository.findOne(contentItemId);
		
		if(content != null) {
			ReadingRoomObjectPart parentPart = content.getParentPart();			
			ReadingRoomObject parentObject = parentPart.getParentObject();
			
			if(currentUserCanEdit(parentObject)) {
				parentPart.getContents().remove(content);
				readingRoomPartRepository.save(parentPart);
			} else {
				throw new IllegalAccessException("The current user is not allowed to delete reading room object with id " + parentObject.getId());
			}			
		}		
	}
	
	public boolean currentUserCanEdit(ReadingRoomObject object) {
		boolean canEdit = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(auth != null && object != null && auth.getPrincipal().getClass().isAssignableFrom(User.class)) {
        	User user = (User)auth.getPrincipal();        	
        	boolean isOwner = user.getUsername().equals(object.getInputter());
        	boolean isLevel3 = user.hasRole(Role.Name.ROLE_LEVEL_III_CATALOGER);
        	boolean canEditOwn = user.hasRole(Role.Name.ROLE_LEVEL_II_CATALOGER) || (isLevel3 && !object.isActive());
        	boolean canEditAny = user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER));        	
       		canEdit = (canEditOwn && isOwner) || canEditAny;        	
        }		
		
		return canEdit;
	}	
	
public ReadingRoomSearchResult search(Map<String, String> searchTerms, Pageable pageable) {		
		
		List<ElasticSearchQuery> elasticSearchQueries = new ArrayList<>();
		boolean highlightHits = false;
		boolean isCataloger = VhmmlSecurityUtil.isCurrentUserCataloger();
		boolean isScholar = VhmmlSecurityUtil.isCurrentScholar();
		
		// if the current user isn't a cataloger or scholar, they only see active records
		if(!isCataloger && !isScholar) {
			elasticSearchQueries.add(new TermQuery("active", "true"));
		}
		
		if(isScholar && !isCataloger) {
			BoolQuery boolQuery = new BoolQuery();
			boolQuery.should(new TermQuery("active", "true"));
			boolQuery.should(new TermQuery("assignedScholar", VhmmlSecurityUtil.getCurrentUser().getId().toString()));			
			elasticSearchQueries.add(boolQuery);
		}
		
		for(String field : searchTerms.keySet()) {
			String searchString = searchTerms.get(field);

			if(StringUtils.isNotEmpty(searchString)) {
				// NOTE: hmml project number has been removed from keyword search to reduce the number of irrelevant results, leaving this comment to explain hmml project search below
				// We do a second query on the zero trim version of the hmml project number that is a "should" query. This is because
				// we want matches on the number part of a hmml project number to bubble to the top, even on partial matches.
				// Example: user searches for api 7, this should match apid 00007 the best, however, doing just the cross field "and"
				// search will not match on the 7, this is because a cross fields "and" search means all terms should be found in a field, "api" and "7".
				// Only the "7" part will match the zero trimmed version of the hmml project number because it's not filtered using the ngram filter,
				// so the partial match doesn't hit (the zero_trim version is "apid 7", not "api 7"). Using an "or" boolean search means it will be a 
				// hit if either "api" OR "7" appears in the term, which will bubble the apid 00007 to the top because the first query will find a
				// match for "api" on the regular hmmlProjectNumber (ngram does partial match) and the second query against the zero_trim version of the field will match on the 7.				
				String[] zeroTrimFields = new String[] {"hmmlProjectNumber.zero_trim", "hmmlProjectNumber.zero_trim_folded", "alternateSurrogates.zero_trim", "alternateSurrogates.zero_trim_folded", "shelfmark.zero_trim", "shelfmark.zero_trim_folded"};
				
				switch(field) {
					case "keyword":
						boolean exactSearch = searchString.startsWith("\"") && searchString.endsWith("\"");
						boolean wildcardSearch = searchString.contains("*");
						
						if(exactSearch) {
							elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getKeywordExactMatchFields(), MultiMatchQueryBuilder.Type.PHRASE, 0));
						} else if(wildcardSearch) {
							elasticSearchQueries.add(new WildcardQuery(searchString, SearchField.getKeywordFields()));
							// add an exact search for the search string with the wildcards stripped, this will make searches
							// for things like christ* push the hits with an exact match on "christ" to the top
							String strippedSeachString = searchString.replaceAll("\\*", "").replaceAll("\\?", "");
							elasticSearchQueries.add(new MultiMatchQuery(strippedSeachString, SearchField.getKeywordExactMatchFields(), MultiMatchQueryBuilder.Type.PHRASE, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, BoolOperation.SHOULD));
						} else {
							String[] searchStringTokens = StringUtils.split(searchString);
							// if they entered more than one term in the keyword field, then we do a "should" operation, meaning one of the search terms should appear in a field, (a "must" would require every search term to be in a single field)
							// if they only entered one search term, then we do a "must" operation because the search term must appear in a field, otherwise it's not filtering based on the search term they entered at all
							// because it doesn't necessarily have to match any fields to be considered a match
							BoolOperation boolOperation = searchStringTokens.length > 1 ? BoolOperation.SHOULD : BoolOperation.MUST;
							elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getKeywordFields(), MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, boolOperation));							
						}
						
						highlightHits = true;						
						break;
					case "beginDate":
						elasticSearchQueries.add(new MultipleFieldRangeQuery(searchTerms.get("endDate"), searchString, new String[] {"beginDate", "endDate"}));						
						break;
					case "endDate":
						// endDate gets handled with begin date
						break;
					case "downloadable":
					case "active":						
					case "objectType":
						// if they are looking for manuscripts or print item, also include items that have both manuscript/print content
						if(searchString.equals("MANUSCRIPT") || searchString.equals("PRINT")) {
							elasticSearchQueries.add(new TermsQuery(field, new String[]{searchString, "MANUSCRIPT_PRINT"}));
						} else {
							elasticSearchQueries.add(new TermQuery(field, searchString));
						}
						break;
					case "format":
						// we search terms queries against the raw field that's not analyzed because we don't want the search text broken into tokens
						// for example, searching for "Scanned Microfilm" wouldn't bring anything searching against the analyzed field because it doesn't 
						// match the analyzed field that would be broken into the 2 tokens "Scanned" and "Microfilm"
						elasticSearchQueries.add(new TermsQuery(field + ".raw", searchString.toLowerCase().split(",")));
						break;
					case "language":
					case "genres":
					case "features":
					case "script":
					case "writingSystem":
						elasticSearchQueries.add(
							new BoolMultiMatchQuery(
								SearchField.getFieldsForGroup(field), 
								searchString.split("</search-term>"), // we split on this weird token because fields like feature have things like commas in the values 
								MultiMatchQueryBuilder.Type.PHRASE, 
								org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND,
								BoolOperation.SHOULD));
						break;
					case "shelfmark":

						zeroTrimFields = new String[] {"shelfmark.zero_trim", "shelfmark.zero_trim_folded"};
						elasticSearchQueries.add(new MultiMatchQuery(searchString, zeroTrimFields, MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.OR, BoolOperation.SHOULD));
						
						// extra phrase "AND" query add to the "should" part of the query to push exact matches to the top, e.g. a search for "CCM 00004" should have CCM 00004 come before CCM 00040
						elasticSearchQueries.add(new MultiMatchQuery(searchString, zeroTrimFields, MultiMatchQueryBuilder.Type.PHRASE, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, BoolOperation.SHOULD));
						
						// additional query against zero trimmed fields, see keyword search above for explanation
						elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getFieldsForGroup(field), MultiMatchQueryBuilder.Type.PHRASE_PREFIX));						
						break;
					case "hmmlProjectNumber":
						zeroTrimFields = new String[] {"hmmlProjectNumber.zero_trim", "hmmlProjectNumber.zero_trim_folded", "alternateSurrogates.zero_trim", "alternateSurrogates.zero_trim_folded"}; 
						elasticSearchQueries.add(new MultiMatchQuery(searchString, zeroTrimFields, MultiMatchQueryBuilder.Type.CROSS_FIELDS, org.elasticsearch.index.query.MatchQueryBuilder.Operator.OR, BoolOperation.SHOULD));
						
						// extra phrase "AND" query add to the "should" part of the query to push exact matches to the top, e.g. a search for "CCM 00004" should have CCM 00004 come before CCM 00040 
						elasticSearchQueries.add(new MultiMatchQuery(searchString, zeroTrimFields, MultiMatchQueryBuilder.Type.PHRASE, org.elasticsearch.index.query.MatchQueryBuilder.Operator.AND, BoolOperation.SHOULD));
						// additional query against zero trimmed fields, see keyword search above for explanation
						elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getFieldsForGroup(field), MultiMatchQueryBuilder.Type.PHRASE_PREFIX));												
						break;
					default:
						elasticSearchQueries.add(new MultiMatchQuery(searchString, SearchField.getFieldsForGroup(field), MultiMatchQueryBuilder.Type.PHRASE_PREFIX));
				}				
			}					
		}
		
		SearchResponse searchResponse = elasticSearchService.multiFieldSearch( 
			IndexedType.READING_ROOM_OBJECT, 
			elasticSearchQueries, 
			highlightHits,
			pageable);
		        
		return getSearchResult(searchResponse, pageable);
	}
	
	public boolean checkImageAccess(Long projectId) {
		ReadingRoomObject object = findById(projectId);		
		return checkImageAccess(object);
	}

	public boolean checkImageAccess(String hmmlProjectNumber) {
		ReadingRoomObject object = findByHmmlProject(hmmlProjectNumber);		
		return checkImageAccess(object);
	}
	
	public boolean mustAcceptUsageAgreement(Long objectId) {
		boolean mustAcceptAgreement = true;
		ReadingRoomObject object = findById(objectId);
		
		if(object != null && object.getAccessRestriction() == AccessRestriction.UNREGISTERED) {
			mustAcceptAgreement = false;
		}
		
		return mustAcceptAgreement;
	}
	
	private boolean checkImageAccess(ReadingRoomObject object) {
		boolean hasAccess = false;
		
		try {			
			
			if(object != null) {
				AccessRestriction accessRestriction = object.getAccessRestriction();			
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();			
				
				if(principal instanceof User) {		
					hasAccess = accessRestriction != AccessRestriction.ON_SITE_ONLY && accessRestriction != AccessRestriction.ON_SITE_ONLY_ORDER_SCAN;
				} else {
					hasAccess = accessRestriction == AccessRestriction.UNREGISTERED; 
				}
			}						
		} catch (Exception e) {
			LOG.error("Exception attempting to retrieve image access permissions for object with id " + object.getId(), e);
		}
		
		return hasAccess;
	}
	
	private ReadingRoomSearchResult getSearchResult(SearchResponse searchResponse, Pageable pageable) {
		ReadingRoomSearchResult searchResult = new ReadingRoomSearchResult();
		SearchHit[] hits = searchResponse.getHits().getHits();
        List<ReadingRoomSearchHit> searchHits = new ArrayList<ReadingRoomSearchHit>();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        User user = null;        
    	boolean canEditAny = false;
    	boolean level2Cataloger = false;
    	boolean level3Cataloger = false;
        
        if(principal.getClass().isAssignableFrom(User.class)) {
        	user = (User)principal;
        	canEditAny = user != null && user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER));
        	level2Cataloger = user != null && user.hasRole(Role.Name.ROLE_LEVEL_II_CATALOGER);
        	level3Cataloger = user.hasRole(Role.Name.ROLE_LEVEL_III_CATALOGER);        	
        }
        
        for(SearchHit hit : hits) {
        	ReadingRoomSearchHit searchHit = new ReadingRoomSearchHit(); 
        	Map<String, Object> source = hit.getSource();
        	String inputter = (String)source.get("inputter");
        	boolean isOwner = user != null && user.getUsername().equals(inputter);
        	// level 3 catalogers can edit objects they created only if they are still pending
        	boolean isActive = (Boolean)source.get("active");
        	boolean canEditOwn = level2Cataloger || (level3Cataloger && !isActive);
       		searchHit.setEditable((canEditOwn && isOwner) || canEditAny);

        	Map<String, HighlightField> highlightFields = hit.getHighlightFields();
        	ReadingRoomObjectOverview readingRoomObjectOverview = new ReadingRoomObjectOverview();
        	readingRoomObjectOverview.setDownloadable((Boolean)source.get("downloadable"));
        	readingRoomObjectOverview.setId(new Long((Integer)source.get("id")));        	
        	readingRoomObjectOverview.setTitles(ElasticSearchUtil.getFieldValue(source, highlightFields, "titles"));
        	readingRoomObjectOverview.setAlternateTitles(ElasticSearchUtil.getFieldValue(source, highlightFields, "alternateTitles"));
        	readingRoomObjectOverview.setUniformTitles(ElasticSearchUtil.getFieldValue(source, highlightFields, "uniformTitles"));
        	readingRoomObjectOverview.setShelfmark(ElasticSearchUtil.getFieldValue(source, highlightFields, "shelfmark"));
        	readingRoomObjectOverview.setHmmlProjectNumber(ElasticSearchUtil.getFieldValue(source, highlightFields, "hmmlProjectNumber"));
        	readingRoomObjectOverview.setInputter(ElasticSearchUtil.getFieldValue(source, highlightFields, "inputter"));
        	readingRoomObjectOverview.setInputDate(ElasticSearchUtil.getFieldValue(source, highlightFields, "inputDate"));
        	readingRoomObjectOverview.setObjectType(ElasticSearchUtil.getFieldValue(source, highlightFields, "objectType"));
        	readingRoomObjectOverview.setExtent(ElasticSearchUtil.getFieldValue(source, highlightFields, "extent"));
        	readingRoomObjectOverview.setProvenance(ElasticSearchUtil.getFieldValue(source, highlightFields, "provenance"));
        	readingRoomObjectOverview.setBinding(ElasticSearchUtil.getFieldValue(source, highlightFields, "binding"));
        	readingRoomObjectOverview.setProcessedBy(ElasticSearchUtil.getFieldValue(source, highlightFields, "processedBy"));
        	readingRoomObjectOverview.setAccessRestriction(ElasticSearchUtil.getFieldValue(source, highlightFields, "accessRestriction"));
        	readingRoomObjectOverview.setFormat(ElasticSearchUtil.getFieldValue(source, highlightFields, "format"));
        	readingRoomObjectOverview.setCountry(ElasticSearchUtil.getFieldValue(source, highlightFields, "country"));
        	readingRoomObjectOverview.setCity(ElasticSearchUtil.getFieldValue(source, highlightFields, "city"));
        	readingRoomObjectOverview.setRepository(ElasticSearchUtil.getFieldValue(source, highlightFields, "repository"));
        	readingRoomObjectOverview.setCenturies(ElasticSearchUtil.getFieldValue(source, highlightFields, "centuries"));
        	readingRoomObjectOverview.setSupport(ElasticSearchUtil.getFieldValue(source, highlightFields, "support"));
        	readingRoomObjectOverview.setDimensions(ElasticSearchUtil.getFieldValue(source, highlightFields, "dimensions"));
        	readingRoomObjectOverview.setLanguages(ElasticSearchUtil.getFieldValue(source, highlightFields, "languages"));
        	readingRoomObjectOverview.setAuthors(ElasticSearchUtil.getFieldValue(source, highlightFields, "authors"));
        	readingRoomObjectOverview.setOtherContributors(ElasticSearchUtil.getFieldValue(source, highlightFields, "otherContributors"));
        	readingRoomObjectOverview.setSubjects(ElasticSearchUtil.getFieldValue(source, highlightFields, "subjects"));
        	readingRoomObjectOverview.setGenres(ElasticSearchUtil.getFieldValue(source, highlightFields, "genres"));
        	readingRoomObjectOverview.setFirstCentury((Integer)source.get("firstCentury"));
        	readingRoomObjectOverview.setLastCentury((Integer)source.get("lastCentury"));
        	readingRoomObjectOverview.setCenturyUncertain((Boolean)source.get("centuryUncertain"));
        	readingRoomObjectOverview.setCenturyList(ElasticSearchUtil.getFieldValue(source, highlightFields, "centuryList"));
        	readingRoomObjectOverview.setPlaceOfOrigin(ElasticSearchUtil.getFieldValue(source, highlightFields, "placeOfOrigin"));
        	readingRoomObjectOverview.setCommonName(ElasticSearchUtil.getFieldValue(source, highlightFields, "commonName"));
        	readingRoomObjectOverview.setNotes(ElasticSearchUtil.getFieldValue(source, highlightFields, "notes"));
        	readingRoomObjectOverview.setActive(isActive);
        	Object locked = source.get("locked"); 
        	readingRoomObjectOverview.setLocked(locked != null ? (Boolean)locked : new Boolean(false));
        	readingRoomObjectOverview.setLockedBy(ElasticSearchUtil.getFieldValue(source, highlightFields, "lockedBy"));
        	readingRoomObjectOverview.setIconName((String)source.get("iconName"));   
        	readingRoomObjectOverview.setDecoration((String)source.get("decoration"));   
        	        	        	
        	searchHit.setObjectOverview(readingRoomObjectOverview);        	
        	searchHit.setHighlightFields(ElasticSearchUtil.getHighlightFieldValues(source, highlightFields));
        	searchHits.add(searchHit);
        }		
		
        searchResult.setFieldDisplayNames(SearchField.getDisplayNameMap());
        searchResult.setSearchHits(searchHits);        
		searchResult.setPagingInformation(searchResponse, pageable);
		
		return searchResult;
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjects(List<ReadingRoomObject> readingRoomObjects) {
		readingRoomObjectRepository.batchSave(readingRoomObjects);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectParts(List<ReadingRoomObjectPart> readingRoomObjectParts) {
		readingRoomPartRepository.batchSave(readingRoomObjectParts);
	}
	
	public void bulkInsertReadingRoomPartContributors(List<ReadingRoomPartContributor> readingRoomPartContributors) {
		readingRoomPartContributorRepository.batchSave(readingRoomPartContributors);
	}
	
	@Transactional
	public void bulkInsertReadingRoomContent(List<Content> contents) {
		readingRoomContentRepository.batchSave(contents);
	}
	
	@Transactional
	public void bulkInsertReadingRoomContentLanguages(List<ContentLanguage> readingRoomContentLangs) {
		readingRoomContentLangRepository.batchSave(readingRoomContentLangs);
	}
	
	@Transactional
	public void bulkInsertReadingRoomContentSubjects(List<ReadingRoomObjectSubject> readingRoomObjectSubjects) {
		readingRoomObjectSubjectRepository.batchSave(readingRoomObjectSubjects);
	}
	
	@Transactional
	public void bulkInsertReadingRoomImage(List<Image> images) {
		readingRoomImageRepository.batchSave(images);
	}
	
	@Transactional
	public void bulkInsertReadingRoomContentContributors(List<ContentContributor> contentContributors) {
		contentContributorRepository.batchSave(contentContributors);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectExtents(List<Extent> extents) {
		extentRepository.batchSave(extents);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectContributors(List<ReadingRoomObjectContributor> objectContributors) {
		readingRoomObjectContributorRepository.batchSave(objectContributors);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectAltSurrogates(List<AlternateSurrogate> altSurrogates) {		
		alternateSurrogateRepository.batchSave(altSurrogates);
	}

	@Transactional
	public void bulkInsertReadingRoomObjectGenres(List<ReadingRoomObjectGenre> objectGenres) {
		readingRoomObjectGenreRepository.batchSave(objectGenres);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectSubjects(List<ReadingRoomObjectSubject> objectSubjects) {
		readingRoomObjectSubjectRepository.batchSave(objectSubjects);
	}		
	
	@Transactional
	public void bulkInsertReadingRoomObjectFeatures(List<ReadingRoomObjectFeature> objectFeatures) {
		readingRoomObjectFeatureRepository.batchSave(objectFeatures);
	}

	@Transactional
	public void bulkInsertReadingRoomObjectBibUrls(List<BibliographyUrl> bibUrls) {
		readingRoomObjectBibUrlRepository.batchSave(bibUrls);
	}
	
	@Transactional
	public void bulkInsertReadingRoomObjectFacsimileUrl(List<FacsimileUrl> facsimileUrls) {
		readingRoomObjectFacsimileUrlRepository.batchSave(facsimileUrls);
	}
	
	@Transactional
	public void bulkInsertArchivalData(List<ArchivalData> archivalData) {
		archivalDataRepository.batchSave(archivalData);
	}
	
	@Transactional
	public void bulkInsertArchivalContributors(List<ArchivalContributor> archivalContributors) {
		archivalContributorRepository.batchSave(archivalContributors);
	}
	
	public void bulkInsertArchivalLanguages(List<ArchivalDataLanguage> archivalLanguages) {
		archivalDataLanguageRepository.batchSave(archivalLanguages);
	}
	
	private String getSupportList(List<ReadingRoomObjectPart> parts) {
		StringBuilder supportList = new StringBuilder();
		
		if(CollectionUtils.isNotEmpty(parts)) {			
			Map<String, Support> supports = new HashMap<String, Support>();
			
			for(ReadingRoomObjectPart part : parts) {
				List<String> supportNames = AttributeUtil.getStringAttributeAsList(part.getSupport());
				
				if(CollectionUtils.isNotEmpty(supportNames)) {
					for(String supportName : supportNames) {
						Support support = Support.valueOf(supportName);
						
						if(!supports.containsKey(supportName)) {
							supports.put(supportName, support);
						}
					}
				}
			}
			
			if(MapUtils.isNotEmpty(supports)) {
				List<Support> supportValues = Lists.newArrayList(supports.values());
				
				for(int i = 0; i < supportValues.size(); i++) {
					if(i > 0) {
						
						if(i + 1 < supportValues.size()) {
							supportList.append(", ");
						} else {
							supportList.append(" and ");
						}						
					}
					
					supportList.append(supportValues.get(i).getName());
				}				
			}
		}
		
		return supportList.toString();
	}
	
	private List<ReadingRoomObjectContributor> findOrCreateContributors(List<ReadingRoomObjectContributor> objectContributors) throws IOException {
		
		if(CollectionUtils.isNotEmpty(objectContributors)) {
			for(ReadingRoomObjectContributor objectContributor : objectContributors) {
				Contributor dbContributor = findOrCreateContributor(objectContributor.getContributor());
				objectContributor.setContributor(dbContributor);
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_CONTRIBUTOR, dbContributor.getId().toString(), objectMapper.writeValueAsString(dbContributor));
			}
		}		
		
		return objectContributors;	
	}
	
	private Contributor findOrCreateContributor(Contributor contributor) {
		
		Contributor dbContributor = null;
		
		if(contributor != null) {
			// contributors need name AND display name the same to be considered unique because occasionally catalogers put the name in the 
			// display name and leave the regular name blank			
			dbContributor = readingRoomContributorRepository.findByNameAndDisplayName(contributor.getName(), contributor.getDisplayName());
			
			if(dbContributor != null) {
				BeanUtils.copyProperties(contributor, dbContributor, "id");			
			} else {
				dbContributor = readingRoomContributorRepository.save(contributor);					
			}
		}
		
		
		return dbContributor;
	}
	
	private List<ContentContributor> findOrCreateContentContributors(List<ContentContributor> contentContributors) throws IOException {
		
		if(CollectionUtils.isNotEmpty(contentContributors)) {
			for(ContentContributor contentContributor : contentContributors) {
				Contributor dbContributor = findOrCreateContributor(contentContributor.getContributor());
				contentContributor.setContributor(dbContributor);
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_CONTRIBUTOR, dbContributor.getId().toString(), objectMapper.writeValueAsString(dbContributor));
			}
		}		
		
		return contentContributors;	
	}
	
	private List<ReadingRoomPartContributor> findOrCreatePartContributors(List<ReadingRoomPartContributor> partContributors) throws IOException {
		
		if(CollectionUtils.isNotEmpty(partContributors)) {
			for(ReadingRoomPartContributor partContributor : partContributors) {
				Contributor dbContributor = findOrCreateContributor(partContributor.getContributor());
				partContributor.setContributor(dbContributor);
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_CONTRIBUTOR, dbContributor.getId().toString(), objectMapper.writeValueAsString(dbContributor));
			}
		}		
		
		return partContributors;	
	}
	
	private List<ArchivalContributor> findOrCreateArchivalContributors(List<ArchivalContributor> archivalContributors) throws IOException {
		
		if(CollectionUtils.isNotEmpty(archivalContributors)) {
			for(ArchivalContributor archivalContributor : archivalContributors) {
				Contributor dbContributor = findOrCreateContributor(archivalContributor.getContributor());
				archivalContributor.setContributor(dbContributor);
				elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_CONTRIBUTOR, dbContributor.getId().toString(), objectMapper.writeValueAsString(dbContributor));
			}
		}		
		
		return archivalContributors;	
	}
	
	private void findOrCreateArchivalData(ReadingRoomObject object) throws IOException {
		
		if(object instanceof ArchivalObject) {
			ArchivalData archivalData = ((ArchivalObject)object).getArchivalData();
			
			if(archivalData != null) {
				
				if(CollectionUtils.isNotEmpty(archivalData.getAssociatedNames())) {
					archivalData.setAssociatedNames(findOrCreateArchivalContributors(archivalData.getAssociatedNames()));					
				}
				
				List<ArchivalContent> content = archivalData.getContent();
				
				if(CollectionUtils.isNotEmpty(content)) {
					for(ArchivalContent nextContent : content) {
						nextContent.setContentContributors(findOrCreateContentContributors(nextContent.getContentContributors()));
					}
				}				
			}			
		}
	}
}
