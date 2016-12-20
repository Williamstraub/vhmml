package org.vhmml.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalContributor;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalDataLanguage;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.ContentLanguage;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Country;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.entity.readingroom.Feature;
import org.vhmml.entity.readingroom.Genre;
import org.vhmml.entity.readingroom.Image;
import org.vhmml.entity.readingroom.Import;
import org.vhmml.entity.readingroom.Institution;
import org.vhmml.entity.readingroom.Language;
import org.vhmml.entity.readingroom.Manuscript;
import org.vhmml.entity.readingroom.ManuscriptAndPrinted;
import org.vhmml.entity.readingroom.PrintedObject;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObject.AccessRestriction;
import org.vhmml.entity.readingroom.ReadingRoomObject.Status;
import org.vhmml.entity.readingroom.ReadingRoomObject.Type;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.entity.readingroom.ReadingRoomObjectFeature;
import org.vhmml.entity.readingroom.ReadingRoomObjectGenre;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.ReadingRoomObjectSubject;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.entity.readingroom.Subject;
import org.vhmml.entity.readingroom.Support;
import org.vhmml.entity.readingroom.UniformTitle;
import org.vhmml.entity.readingroom.WritingSystem;
import org.vhmml.repository.CityRepository;
import org.vhmml.repository.CountryRepository;
import org.vhmml.repository.readingroom.AlternateTitleRepository;
import org.vhmml.repository.readingroom.ArchivalContributorRepository;
import org.vhmml.repository.readingroom.ArchivalDataLanguageRepository;
import org.vhmml.repository.readingroom.ArchivalDataRepository;
import org.vhmml.repository.readingroom.ImportRepository;
import org.vhmml.repository.readingroom.ReadingRoomAlternateSurrogateRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentLanguageRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentRepository;
import org.vhmml.repository.readingroom.ReadingRoomContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomFeatureRepository;
import org.vhmml.repository.readingroom.ReadingRoomFormatRepository;
import org.vhmml.repository.readingroom.ReadingRoomGenreRepository;
import org.vhmml.repository.readingroom.ReadingRoomImageRepository;
import org.vhmml.repository.readingroom.ReadingRoomInstitutionRepository;
import org.vhmml.repository.readingroom.ReadingRoomLanguageRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectBibUrlRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectExtentRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectFacsimileUrlRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectFeatureRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectGenreRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectSubjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomPartCenturyRepository;
import org.vhmml.repository.readingroom.ReadingRoomPartContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomPartRepository;
import org.vhmml.repository.readingroom.ReadingRoomSubjectRepository;
import org.vhmml.repository.readingroom.RepositoryRepository;
import org.vhmml.repository.readingroom.UniformTitleRepository;
import org.vhmml.repository.readingroom.WritingSystemRepository;
import org.vhmml.util.AttributeUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OliverServiceImpl implements OliverService {

	private static final Logger LOG = Logger.getLogger(OliverServiceImpl.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ReadingRoomObjectRepository readingRoomObjectRepository;
	
	@Autowired
	private ArchivalDataRepository archivalDataRepository;
	
	@Autowired
	private ArchivalContributorRepository archivalContributorRepository;
	
	@Autowired
	private ReadingRoomPartRepository readingRoomPartRepository;
	
	@Autowired
	private ReadingRoomPartContributorRepository readingRoomPartContributorRepository;
	
	@Autowired
	private ReadingRoomPartCenturyRepository readingRoomPartCenturyRepository;
	
	@Autowired
	private ReadingRoomContentRepository readingRoomContentRepository;
	
	@Autowired
	private ReadingRoomContentContributorRepository contentContributorRepository;
	
	@Autowired
	private ReadingRoomContentLanguageRepository contentLanguageRepository;
	
	@Autowired
	private ReadingRoomImageRepository readingRoomImageRepository;	
	
	@Autowired
	private ReadingRoomLanguageRepository languageRepository;	
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private RepositoryRepository repoRepository;
	
	@Autowired
	private ReadingRoomContentLanguageRepository contentLangRepository;
	
	@Autowired
	private ReadingRoomContributorRepository contributorRepository;
	
	@Autowired
	private ReadingRoomGenreRepository genreRepository;
	
	@Autowired
	private UniformTitleRepository uniformTitleRepository;
	
	@Autowired
	private ReadingRoomSubjectRepository subjectRepository;
	
	@Autowired
	private ReadingRoomFormatRepository formatRepository;
	
	@Autowired
	private WritingSystemRepository writingSystemRepository;
	
	@Autowired
	private ReadingRoomInstitutionRepository institutionRepository;	
	
	@Autowired
	private ReadingRoomObjectGenreRepository objectGenreRepository;
	
	@Autowired
	private ReadingRoomObjectSubjectRepository objectSubjectRepository;
	
	@Autowired
	private AlternateTitleRepository alternateTitleRepository;
	
	@Autowired
	private ReadingRoomAlternateSurrogateRepository alternateSurrogateRepository;
	
	@Autowired
	private ReadingRoomObjectBibUrlRepository bibUrlRepository;
	
	@Autowired
	private ReadingRoomObjectFacsimileUrlRepository facsimileUrlRepository;
	
	@Autowired
	private ReadingRoomObjectContributorRepository objectContributorRepository;
	
	@Autowired
	private ReadingRoomObjectExtentRepository objectExtentRepository;
	
	@Autowired
	private ReadingRoomFeatureRepository featureRepository;
	
	@Autowired
	private ReadingRoomObjectFeatureRepository objectFeatureRepository;
	
	@Autowired
	private ArchivalDataLanguageRepository archivalDataLanguageRepository;
	
	@Autowired
	private ImportRepository importRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	private static Map<String, Country> countryCache = new HashMap<>();	
	private static Map<City, City> cityCache = new HashMap<>();
	private static Map<Repository, Repository> repoCache = new HashMap<>();
	private static Map<String, Language> languageCache = new HashMap<>();
	private static Map<String, WritingSystem> writingSystemCache = new HashMap<>();
	private static Map<String, Contributor> contributorCache = new HashMap<>();
	private static Map<String, Genre> genreCache = new HashMap<>();
	private static Map<String, UniformTitle> uniformTitleCache = new HashMap<>();
	private static Map<String, Subject> subjectCache = new HashMap<>();
	private static Map<String, Institution> institutionCache = new HashMap<>();	
	private static Map<String, Feature> featureCache = new HashMap<>();	
	
	public List<Import> getImports() {
		return Lists.newArrayList(importRepository.findAllByOrderByImportDateDesc());
	}
	
	@Transactional
	public void deleteImportedData(Long importId) {
		Import importResult = getImportResult(importId);
		
		if(importResult != null) {
			String importFileName = importResult.getImportFile();
			List<Integer> objectIds = readingRoomObjectRepository.selectObjectIdsByDataSource(importFileName);			
			readingRoomImageRepository.deleteByImportSource(importFileName);
			contentContributorRepository.deleteByImportSource(importFileName);
			contentLangRepository.deleteByImportSource(importFileName);
			alternateTitleRepository.deleteByImportSource(importFileName);
			readingRoomContentRepository.deleteByImportSource(importFileName);
			alternateSurrogateRepository.deleteByImportSource(importFileName);
			bibUrlRepository.deleteByImportSource(importFileName);
			facsimileUrlRepository.deleteByImportSource(importFileName);
			objectContributorRepository.deleteByImportSource(importFileName);
			objectExtentRepository.deleteByImportSource(importFileName);
			objectFeatureRepository.deleteByImportSource(importFileName);
			objectGenreRepository.deleteByImportSource(importFileName);
			objectSubjectRepository.deleteByImportSource(importFileName);
			readingRoomPartContributorRepository.deleteByImportSource(importFileName);
			readingRoomPartCenturyRepository.deleteByImportSource(importFileName);
			readingRoomPartRepository.deleteByImportSource(importFileName);			
			archivalDataLanguageRepository.deleteByImportSource(importFileName);
			archivalContributorRepository.deleteByImportSource(importFileName);
			archivalDataRepository.deleteByImportSource(importFileName);
			readingRoomObjectRepository.deleteByImportSource(importFileName);			
			importResult.setStatus(Import.Status.DELETED);
			importRepository.save(importResult);
			elasticSearchService.delete(IndexedType.READING_ROOM_OBJECT, objectIds);
		}
	}
	
	public Import getImportResult(Long id) {
		return importRepository.findOne(id);
	}
	
	public Import createImportJob(String databaseLocation) {
		Import importJob = new Import();
		importJob.setImportFile(databaseLocation);
		importJob.setStatus(Import.Status.CREATED);
		
		return importRepository.save(importJob);
	}

	@Transactional
	public Import runImportJob(Import importJob) {
		
		clearCache();
		String databaseLocation = importJob.getImportFile();
		Date now = new Date();
		Long start = now.getTime();
		importJob.setStatus(Import.Status.STARTED);
		importJob.setImportDate(now);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;		
		
		try {
			importJob.setObjectsBeforeImport(new Long(readingRoomObjectRepository.count()).intValue());
			importJob.setPartsBeforeImport(new Long(readingRoomPartRepository.count()).intValue());
			importJob.setContentsBeforeImport(new Long(readingRoomContentRepository.count()).intValue());
			importJob.setImagesBeforeImport(new Long(readingRoomImageRepository.count()).intValue());
			importJob.setContentContributorsBeforeImport(new Long(contentContributorRepository.count()).intValue());
			importJob.setContentLanguagesBeforeImport(new Long(contentLanguageRepository.count()).intValue());
			
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			conn = DriverManager.getConnection("jdbc:ucanaccess://" + databaseLocation);
			stmt = conn.createStatement();
			
			// grab these first, need to check the header records to see if it's archival or not because we only import header records for archival
			rs = stmt.executeQuery("select * from tblMs");
			rs.next();
			String recordTypeName = StringUtils.trim(rs.getString("RecordType"));
			
			Map<Long, List<ReadingRoomObjectPart>> partsByObjectId = new HashMap<>();
			
			if(!ReadingRoomObject.Type.ARCHIVAL_OBJECT.getDisplayName().equals(recordTypeName)) {
				rs = stmt.executeQuery("select * from tblLink3");
				Map<Long, List<Image>> imagesByContentId = importReadingRoomImages(rs, importJob);
				
				rs = stmt.executeQuery("select * from tblEDoc");			
				Map<Long, List<Content>> contentByPartId = importReadingRoomContent(rs, importJob);
				
				rs = stmt.executeQuery("select * from tblMsPt");		    
				partsByObjectId = importReadingRoomObjectParts(rs, importJob, contentByPartId, imagesByContentId);
			}			
			
			// have to re-read all the header records because the MS Access driver doesn't support the first() or previous() methods on the result set object
			rs = stmt.executeQuery("select * from tblMs");
			importReadingRoomObjects(rs, importJob, partsByObjectId, recordTypeName);
			
			importJob.setObjectsAfterImport(new Long(readingRoomObjectRepository.count()).intValue());
			importJob.setPartsAfterImport(new Long(readingRoomPartRepository.count()).intValue());
			importJob.setContentsAfterImport(new Long(readingRoomContentRepository.count()).intValue());
			importJob.setImagesAfterImport(new Long(readingRoomImageRepository.count()).intValue());
			importJob.setContentContributorsAfterImport(new Long(contentContributorRepository.count()).intValue());
			importJob.setContentLanguagesAfterImport(new Long(contentLanguageRepository.count()).intValue());
			importJob.setDurationSeconds((new Date().getTime() - start)/1000);			
			elasticSearchService.reindex(IndexedType.READING_ROOM_OBJECT);
			importJob.setStatus(Import.Status.COMPLETED);
			saveImportJob(importJob);
		} catch (Exception e) {			
			LOG.error("Unexpected exception attempting to import data from location " + databaseLocation, e);
			throw new RuntimeException(e);
		} finally {			
			try {				
				
				if(rs != null) {
					rs.close();
				}
				
				if(stmt != null) {
					stmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				LOG.error("Exception closing resource after import of file " + databaseLocation, e);
				throw new RuntimeException(e);
			}			
		}		
		
		LOG.info("import of " + databaseLocation + " took " + importJob.getDurationSeconds());
		
		return importJob;		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveImportJob(Import importJob) {
		importRepository.save(importJob);
	}
	
	private List<ReadingRoomObject> importReadingRoomObjects(ResultSet rs, Import importResult, Map<Long, List<ReadingRoomObjectPart>> partsByObjectId, String expectedRecordType) throws Exception {
		List<ReadingRoomObject> readingRoomObjects = new ArrayList<>();
		List<Extent> extents = new ArrayList<Extent>();
		List<ReadingRoomObjectGenre> genres = new ArrayList<>();
		List<ReadingRoomObjectFeature> features = new ArrayList<>();
		List<ReadingRoomObjectSubject> subjects = new ArrayList<>();
		List<ReadingRoomObjectContributor> formerOwners = new ArrayList<>();
		List<AlternateSurrogate> altSurrogates = new ArrayList<>();
		List<BibliographyUrl> bibUrls = new ArrayList<>();
		List<FacsimileUrl> facsimileUrls = new ArrayList<>();		
		List<ArchivalData> archivalDataList = new ArrayList<>();
		List<ArchivalContributor> archivalContributors = new ArrayList<>();
		List<ArchivalDataLanguage> archivalLanguages = new ArrayList<>();
		
		// colophon was on the object in the old database, it's on the part now, need to pull the colophon
		// off the object and set it on the first part for the object
		List<ReadingRoomObjectPart> updatedParts = new ArrayList<>();
		String importFileName = importResult.getImportFile();		
		
		while(rs.next()) {
			Long id = rs.getLong("MsID");			
			String recordTypeName = StringUtils.trim(rs.getString("RecordType"));
			
			// for archival object imports, all records must be archival because we only import header records for archival, therefore if
			// there are archival items mixed in with manuscript/print records we want to fail the whole import
			boolean unexpectedNonArchival = "Archival Material".equalsIgnoreCase(expectedRecordType) && !recordTypeName.equals(expectedRecordType);
			boolean unexpectedArchival = !"Archival Material".equalsIgnoreCase(expectedRecordType) && "Archival Material".equalsIgnoreCase(recordTypeName);
			
			if(unexpectedNonArchival || unexpectedArchival) {
				throw new RuntimeException("An import cannot contain both archival records and non-archival records.  Invalid record type on record with ID " + id + ", expected " + expectedRecordType + " but was " + recordTypeName);
			}
			
			ReadingRoomObject.Type recordType = StringUtils.isNotEmpty(recordTypeName) ? Type.forDisplayName(recordTypeName) : Type.READING_ROOM_OBJECT; 			
			ReadingRoomObject readingRoomObject = new ReadingRoomObject();			
			
			switch(recordType) {
				case READING_ROOM_OBJECT:					
					break;
				case MANUSCRIPT:				
					readingRoomObject = new Manuscript();
					break;
				case MANUSCRIPT_PRINT:
					readingRoomObject = new ManuscriptAndPrinted();
					break;
				case PRINT:
					readingRoomObject = new PrintedObject();					
					break;
				case ARCHIVAL_OBJECT:
					readingRoomObject = new ArchivalObject();
					// foliation is at the object level for archival because it's normally at the part level in Oliver, but archival items have no parts
//					readingRoomObject.setFoliation(StringUtils.trim(rs.getString("Foliation")));
					archivalDataList.add(getArchivalData(rs));
					archivalContributors.addAll(getArchivalContributors(rs));					
					archivalLanguages.addAll(getArchivalDataLanguages(rs));
					break;
			}
			
			readingRoomObject.setDataSource(importFileName);
									
			readingRoomObject.setId(id);		
			Country country = findOrCreateCountry(
					StringUtils.trim(rs.getString("Country")), 
					StringUtils.trim(rs.getString("CountryAuthorityURI_LC")), 
					StringUtils.trim(rs.getString("CountryAuthorityURI_VIAF")), 
					StringUtils.trim(rs.getString("CountryISO3166")));
			City city = findOrCreateCity(country != null ? country.getId() : null, StringUtils.trim(rs.getString("City")), StringUtils.trim(rs.getString("CityAuthorityURI_LC")), StringUtils.trim(rs.getString("CityAuthorityURI_VIAF")));
			readingRoomObject.setCountry(country);
			readingRoomObject.setCity(city);
			readingRoomObject.setProcessedBy(StringUtils.trim(rs.getString("Institution")));
			readingRoomObject.setRepository(findOrCreateRepo(
					city != null ? city.getId() : null, 
					StringUtils.trim(rs.getString("Library")), 
					StringUtils.trim(rs.getString("RepositoryAuthorityURI_LC")), 
					StringUtils.trim(rs.getString("RepositoryAuthorityURI_VIAF"))));
			
			List<Subject> subjectsForObject = findOrCreateSubject(
					StringUtils.trim(rs.getString("Subject")), 
					StringUtils.trim(rs.getString("SubjectURI_LC")), 
					StringUtils.trim(rs.getString("SubjectURI_VIAF")));
			
			if(CollectionUtils.isNotEmpty(subjectsForObject)) {
				for(Subject subject : subjectsForObject) {
					subjects.add(new ReadingRoomObjectSubject(id, subject.getId()));
				}				
			}			
			
			String shelfMark = StringUtils.trim(rs.getString("ShelfMark"));
			
			if(!"NA".equalsIgnoreCase(shelfMark)) {
				readingRoomObject.setShelfMark(shelfMark);
			}
			
			readingRoomObject.setCommonName(StringUtils.trim(rs.getString("NickName")));			
			
			String folio = StringUtils.trim(rs.getString("Folio"));
			
			if(StringUtils.isNotEmpty(folio)  && !"NA".equalsIgnoreCase(folio)) {
				Extent extent = new Extent();
				extent.setFolioImported(folio);
				extent.setReadingRoomObjectId(id);
				extents.add(extent);
			}
			
			readingRoomObject.setCollation(StringUtils.trim(rs.getString("Collation")));			
			readingRoomObject.setPhysicalNotes(StringUtils.trim(rs.getString("PhysicalIssues")));
			readingRoomObject.setBinding(StringUtils.trim(rs.getString("Binding")));
			setBindingDimensions(readingRoomObject, StringUtils.trim(rs.getString("BindingDimensions")));
			
			readingRoomObject.setProvenance(StringUtils.trim(rs.getString("Provenance")));
			readingRoomObject.setBibliography(StringUtils.trim(rs.getString("Bibliography")));
			readingRoomObject.setNotes(StringUtils.trim(rs.getString("Notes")));
			readingRoomObject.setHmmlProjectNumber(StringUtils.trim(rs.getString("Source")));
			// both inputter & input date are stored in the InputterDate field,
			// we're just putting it all in the inputter field for now, clean up work
			// will have to be done by meta data librarian
			readingRoomObject.setInputter(StringUtils.trim(rs.getString("InputterDate")));
			readingRoomObject.setReproductionNotes(StringUtils.trim(rs.getString("Reproduction")));
			readingRoomObject.setAcknowledgments(StringUtils.trim(rs.getString("Acknowledgments")));						
			readingRoomObject.setCaptureDate(rs.getDate("FacsCapDate"));
			
			String accessRestrictionName = StringUtils.trim(rs.getString("AccessRestriction"));
			AccessRestriction accessRestriction = null;
			
			if(StringUtils.isNotEmpty(accessRestrictionName)) {
				accessRestriction = AccessRestriction.forDisplayName(accessRestrictionName);
			}
			
			readingRoomObject.setAccessRestriction(accessRestriction);
			readingRoomObject.setMedium(StringUtils.trim(rs.getString("Medium")));
			readingRoomObject.setViewableOnline(rs.getBoolean("Online"));
			readingRoomObject.setDownloadOption(StringUtils.trim(rs.getString("Download")));			
			readingRoomObject.setIconName(StringUtils.trim(rs.getString("IconName")));
			readingRoomObject.setCiteAs(StringUtils.trim(rs.getString("CiteAs")));
			readingRoomObject.setCurrentStatus(Status.forDisplayName(StringUtils.trim(rs.getString("CurrentStatus"))));
			readingRoomObject.setConditionNotes(StringUtils.trim(rs.getString("ConditionNotes")));			
			readingRoomObject.setSummary(StringUtils.trim(rs.getString("Summary")));			
			
			String featureList = StringUtils.trim(rs.getString("features"));
			features.addAll(getObjectFeatures(id, featureList));
			readingRoomObject.setFeaturesImported(featureList);
			
			String surrogateFormat = StringUtils.trim(rs.getString("SurrogateFormat"));
			String holdingInstitution = StringUtils.trim(rs.getString("HoldingInstitution"));			
			String alternateSurrogate = StringUtils.trim(rs.getString("AlternateSurrogate"));
			String genre = StringUtils.trim(rs.getString("GenreForm"));
			String externalLinkBibUrl = StringUtils.trim(rs.getString("ExternalLinkBib"));
			String externalLinkBibText = StringUtils.trim(rs.getString("ExternalBibText"));
			String externalFacsimileUrl = StringUtils.trim(rs.getString("ExternalLinkFacsimile"));
			String formerOwnersString = StringUtils.trim(rs.getString("FormerOwner"));			
			
			readingRoomObject.setRightToLeft(BooleanUtils.toBoolean(StringUtils.trim(rs.getString("RightToLeft"))));
			
			List<ReadingRoomObjectPart> parts = partsByObjectId.get(id);						
			StringBuilder foliation = new StringBuilder();
			
			if(CollectionUtils.isNotEmpty(parts)) {
				// colophon gets moved from the object level to the part level on import
				String colophon = StringUtils.trim(rs.getString("Colophon"));
				
				if(StringUtils.isNotEmpty(colophon)) {
					ReadingRoomObjectPart firstPart = parts.get(0);
					firstPart.setColophonPart(colophon);					
				}				
				
				for(ReadingRoomObjectPart part : parts) {
					// foliation gets moved up from the part level to the object					
					if(StringUtils.isNotEmpty(part.getFoliationImported())) {
						
						String nextFoliation = part.getFoliationImported().trim();
						
						if(StringUtils.isNotEmpty(nextFoliation)) {
							if(foliation.length() > 0) {
								foliation.append("; ");
							}
							
							foliation.append(nextFoliation);
						}						
					}
					
					ReadingRoomObjectPart.Type partType = ReadingRoomObjectPart.Type.UNKNOWN;
					
					if(recordType == Type.MANUSCRIPT) {
						partType = ReadingRoomObjectPart.Type.MANUSCRIPT;
					} else if(recordType == Type.PRINT) {
						partType = ReadingRoomObjectPart.Type.PRINTED;
					}					
					
					part.setType(partType);
					updatedParts.add(part);
				}							
			}
			
			readingRoomObject.setFoliation(foliation.toString());
			formerOwners.addAll(getFormerOwners(rs));
			
			if(StringUtils.isNotEmpty(surrogateFormat)) {
				readingRoomObject.setSurrogateFormat(formatRepository.findByName(surrogateFormat));
			}
			
			if(StringUtils.isNotEmpty(holdingInstitution)) {
				readingRoomObject.setHoldingInstitution(findOrCreateInstitution(holdingInstitution));
			}
			
			if(StringUtils.isNotEmpty(alternateSurrogate)) {
				altSurrogates.add(new AlternateSurrogate(alternateSurrogate, id));
			}
			
			if(StringUtils.isNotEmpty(genre)) {
				genres.add(new ReadingRoomObjectGenre(id, findOrCreateGenre(genre, StringUtils.trim(rs.getString("GenreFormURI"))).getId()));
			}			
			
			if(StringUtils.isNotEmpty(externalLinkBibUrl)) {
				bibUrls.add(new BibliographyUrl(id, externalLinkBibUrl, externalLinkBibText));
			}
			
			if(StringUtils.isNotEmpty(externalFacsimileUrl)) {				
				facsimileUrls.add(new FacsimileUrl(id, externalFacsimileUrl));
			}			
			
			readingRoomObjects.add(readingRoomObject);
		}
		
		importResult.setObjectsFromSource(readingRoomObjects.size());
		
		if(CollectionUtils.isNotEmpty(formerOwners)) {
			readingRoomService.bulkInsertReadingRoomObjectContributors(formerOwners);
		}
		
		if(CollectionUtils.isNotEmpty(extents)) {
			readingRoomService.bulkInsertReadingRoomObjectExtents(extents);
		}
		
		if(CollectionUtils.isNotEmpty(altSurrogates)) {
			readingRoomService.bulkInsertReadingRoomObjectAltSurrogates(altSurrogates);
		}
		
		if(CollectionUtils.isNotEmpty(genres)) {
			readingRoomService.bulkInsertReadingRoomObjectGenres(genres);
		}			
		
		if(CollectionUtils.isNotEmpty(subjects)) {
			readingRoomService.bulkInsertReadingRoomObjectSubjects(subjects);
		}
		
		if(CollectionUtils.isNotEmpty(bibUrls)) {
			readingRoomService.bulkInsertReadingRoomObjectBibUrls(bibUrls);
		}
		
		if(CollectionUtils.isNotEmpty(facsimileUrls)) {
			readingRoomService.bulkInsertReadingRoomObjectFacsimileUrl(facsimileUrls);
		}
		
		if(CollectionUtils.isNotEmpty(features)) {
			readingRoomService.bulkInsertReadingRoomObjectFeatures(features);
		}
		
		if(CollectionUtils.isNotEmpty(archivalDataList)) {
			readingRoomService.bulkInsertArchivalData(archivalDataList);
		}
		
		if(CollectionUtils.isNotEmpty(archivalContributors)) {
			readingRoomService.bulkInsertArchivalContributors(archivalContributors);
		}
		
		if(CollectionUtils.isNotEmpty(archivalLanguages)) {
			readingRoomService.bulkInsertArchivalLanguages(archivalLanguages);
		}
		
		readingRoomService.bulkInsertReadingRoomObjects(readingRoomObjects);
		
		// have to store these in a separate list because we can't update the parts until after we've saved the object
		readingRoomPartRepository.save(updatedParts);
		
		return readingRoomObjects;
	}
	
	private List<ReadingRoomObjectFeature> getObjectFeatures(Long objectId, String featureList) {
		List<ReadingRoomObjectFeature> features = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(featureList)) {
			String[] featureNames = featureList.split(";");
			
			if(featureNames != null) {			
				for(String featureName : featureNames) {
					featureName = featureName.trim();
					
					if(StringUtils.isNotEmpty(featureName)) {
						Feature feature = featureCache.get(featureName);
						
						if(feature == null) {
							feature = featureRepository.findByName(featureName);
							
							if(feature != null) {
								featureCache.put(featureName, feature);
							}						
						}
						
						if(feature != null) {
							features.add(new ReadingRoomObjectFeature(objectId, feature.getId()));
						}
					}				
				}			
			}
		}		
		
		return features;
	}
	
	private Map<Long, List<ReadingRoomObjectPart>> importReadingRoomObjectParts(ResultSet rs, Import importResult, Map<Long, List<Content>> contentByPart, Map<Long, List<Image>> imagesByContent) throws Exception {
		
		Map<Long, List<ReadingRoomObjectPart>> partsByObjectId = new HashMap<Long, List<ReadingRoomObjectPart>>();
		List<ReadingRoomObjectPart> allParts = new ArrayList<>();
		List<ReadingRoomPartContributor> partContributors = new ArrayList<>();
		 
		while(rs.next()) {
			ReadingRoomObjectPart readingRoomObjectPart = new ReadingRoomObjectPart();
			Long objectId = rs.getLong("MsID");
			Long partId = rs.getLong("MsPtID");
			readingRoomObjectPart.setId(partId);
			readingRoomObjectPart.setParentObject(new ReadingRoomObject(objectId));
			readingRoomObjectPart.setPartNumber(rs.getInt("PartNumber"));
			readingRoomObjectPart.setPartLocation(StringUtils.trim(rs.getString("Folio")));
			readingRoomObjectPart.setFoliationImported(StringUtils.trim(rs.getString("Foliation")));
			String supportDimensions = StringUtils.trim(rs.getString("OuterDimensions"));
			readingRoomObjectPart.setSupportDimensionsImported(supportDimensions);
			setSupportDimensions(readingRoomObjectPart, supportDimensions);
						
			String writingSpace = StringUtils.trim(rs.getString("WritingSpace"));
			readingRoomObjectPart.setWritingSpaceImported(writingSpace);
			setWritingSpace(readingRoomObjectPart, writingSpace);
			
			String countryName = StringUtils.trim(rs.getString("Country"));
			
			if(StringUtils.isNotEmpty(countryName)) {
				readingRoomObjectPart.setCountryOfOriginUncertain(StringUtils.contains(countryName, "?"));			
				readingRoomObjectPart.setCountryOfOrigin(StringUtils.remove(countryName, "(?)").trim());
			}
			
			String cityName = StringUtils.trim(rs.getString("City"));
			
			if(StringUtils.isNotEmpty(cityName)) {
				readingRoomObjectPart.setCityOfOriginUncertain(StringUtils.contains(cityName, "?"));
				readingRoomObjectPart.setCityOfOrigin(StringUtils.remove(cityName, "(?)").trim());
			}
			
			readingRoomObjectPart.setRegionOfOrigin(StringUtils.trim(rs.getString("Region")));
			readingRoomObjectPart.setCardinalOfOrigin(StringUtils.trim(rs.getString("Cardinal")));			
			
			String centuryDate = StringUtils.trim(rs.getString("Date"));
			
			if(!"Unknown".equals(centuryDate)) {
				readingRoomObjectPart.setCenturyImported(centuryDate);
			}
			
			readingRoomObjectPart.setCenturyUncertain(StringUtils.contains(centuryDate, "?"));			
			readingRoomObjectPart.setLayout(StringUtils.trim(rs.getString("Layout")));
			readingRoomObjectPart.setScriptImported(StringUtils.trim(rs.getString("Script")));
			readingRoomObjectPart.setNumberScribes(StringUtils.trim(rs.getString("NumberScribes")));
			String supportList = StringUtils.trim(rs.getString("Support"));
			readingRoomObjectPart.setSupportImported(supportList);
			readingRoomObjectPart.setSupport(getValidSupportString(supportList));
			readingRoomObjectPart.setArtist(StringUtils.trim(rs.getString("Artist")));
			readingRoomObjectPart.setPartNotes(StringUtils.trim(rs.getString("Notes")));
			readingRoomObjectPart.setMedium(StringUtils.trim(rs.getString("Medium")));			
			
			Integer beginDate = rs.getInt("BeginDate");
			Integer endDate = rs.getInt("EndDate");
			
			if(beginDate != 0) {
				readingRoomObjectPart.setBeginDate(beginDate);
			}
			
			if(endDate != 0) {
				readingRoomObjectPart.setEndDate(endDate);
			}			
			
			readingRoomObjectPart.setSeq(rs.getInt("Seq"));
			readingRoomObjectPart.setAcknowledgments(StringUtils.trim(rs.getString("Acknowledgments")));
			readingRoomObjectPart.setWritingSystemImported(StringUtils.trim(rs.getString("Alphabet")));
			readingRoomObjectPart.setNativeDatePrecise(StringUtils.trim(rs.getString("NativeDate")));
			readingRoomObjectPart.setSignatures(rs.getBoolean("Signatures"));
			readingRoomObjectPart.setCatchwords(rs.getBoolean("Catchwords"));
			
			String preciseDate = StringUtils.trim(rs.getString("YearMonthDay"));
			readingRoomObjectPart.setYmdDateImported(preciseDate);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			
			if(StringUtils.isNotEmpty(preciseDate)) {
				try {
										
					Date date = formatter.parse(preciseDate);
					Calendar cal = new GregorianCalendar();
					cal.setTime(date);
					readingRoomObjectPart.setDatePreciseYear(cal.get(Calendar.YEAR));
					readingRoomObjectPart.setDatePreciseMonth(cal.get(Calendar.MONTH));
					readingRoomObjectPart.setDatePreciseDay(cal.get(Calendar.DAY_OF_MONTH));					
				} catch(ParseException e) {
					LOG.warn("Unable to parse YearMonthDate from Oliver, imported value will be stored in ymd_date_imported");
				}
			}
						
			List<String> decorations = new ArrayList<String>();
			
			addNonNullToList(decorations, StringUtils.trim(rs.getString("RepresentationalDecoration")));
			addNonNullToList(decorations, StringUtils.trim(rs.getString("OtherDecoration")));
			
			List<Content> contentsForPart = contentByPart.get(partId);
			readingRoomObjectPart.setContents(contentsForPart);
			
			if(CollectionUtils.isNotEmpty(contentsForPart)) {
				for(Content content : contentsForPart) {
					
					List<Image> images = imagesByContent.get(content.getId());
										
					if(CollectionUtils.isNotEmpty(images)) {						
						for(Image image : images) {
							addNonNullToList(decorations, image.getFolioNumber());
							addNonNullToList(decorations, image.getCaption());						
						}
						
						content.setImages(images);
					}					
				}
			}						
			
			readingRoomObjectPart.setDecoration(StringUtils.join(decorations, "; "));
			
			String scribesString = StringUtils.trim(rs.getString("Scribe"));
			readingRoomObjectPart.setScribe(scribesString);
			
			List<Contributor> scribes = findOrCreateContributor(scribesString, null, null, null);
			
			if(CollectionUtils.isNotEmpty(scribes)) {
				for(Contributor scribe : scribes) {
					partContributors.add(new ReadingRoomPartContributor(readingRoomObjectPart, scribe, Contributor.Type.SCRIBE, StringUtils.trim(rs.getString("ScribeNS"))));
				}				
			}
			
			List<Contributor> artists = findOrCreateContributor(
				StringUtils.trim(rs.getString("Artist")), 
				StringUtils.trim(rs.getString("ArtistURI_LC")), 
				StringUtils.trim(rs.getString("ArtistURI_VIAF")), 
				null);
			
			if(CollectionUtils.isNotEmpty(artists)) {
				for(Contributor artist : artists) {
					partContributors.add(new ReadingRoomPartContributor(readingRoomObjectPart, artist, Contributor.Type.ARTIST, null));
				}				
			}								
			
			List<ReadingRoomObjectPart> partsForObject = partsByObjectId.get(objectId);
			
			if(CollectionUtils.isEmpty(partsForObject)) {
				partsForObject = new ArrayList<ReadingRoomObjectPart>();
			}
			
			partsForObject.add(readingRoomObjectPart);
			partsByObjectId.put(objectId, partsForObject);
			allParts.add(readingRoomObjectPart);			
		}
		
		importResult.setPartsFromSource(allParts.size());
		
		readingRoomService.bulkInsertReadingRoomObjectParts(allParts);
		
		if(CollectionUtils.isNotEmpty(partContributors)) {
			readingRoomService.bulkInsertReadingRoomPartContributors(partContributors);
		}
		
		return partsByObjectId;
	}

	private Map<Long, List<Content>> importReadingRoomContent(ResultSet rs, Import importResult) throws Exception {
		
		Map<Long, List<Content>> contentByPartId = new HashMap<>();
		List<Content> contents = new ArrayList<>();
	    List<ContentLanguage> contentLanguages = new ArrayList<>();
	    List<ContentContributor> contentContributors = new ArrayList<>();
	    
		while(rs.next()) {
			Content content = new Content();
			Long contentId = rs.getLong("EDocID");
			Long partId = rs.getLong("MSptID");
			List<Content> contentForPart = contentByPartId.get(partId);
			
			if(contentForPart == null) {
				contentForPart = new ArrayList<Content>();
			}
			
			content.setId(contentId);			
			content.setParentPart(new ReadingRoomObjectPart(partId));
			content.setItemLocation(StringUtils.trim(rs.getString("Folio")));			
			content.setTitleInMs(StringUtils.trim(rs.getString("Title")));			
			List<String> alternateTitles = new ArrayList<>();			
			String altTitleString = StringUtils.trim(rs.getString("AlternateTitle"));
			
			if(altTitleString != null) {
				String[] altTitles = altTitleString.split(";");
				for(String altTitle : altTitles) {
					alternateTitles.add(altTitle.trim());
				}				
			}
			
			content.setAlternateTitles(alternateTitles);			
			content.setProvisionalTitle(StringUtils.trim(rs.getString("SuppliedTitle")));
			content.setTitleNs(StringUtils.trim(rs.getString("SuppliedTitleNS")));			
			content.setUniformTitle(
				findOrCreateUniformTitle(
					StringUtils.trim(rs.getString("UniformTitle")), 
					StringUtils.trim(rs.getString("UniformTitleURI_LC")), 
					StringUtils.trim(rs.getString("UniformTitleURI_VIAF"))));
			
			content.setItemCondition(StringUtils.trim(rs.getString("StatusOfText")));
			content.setRubric(StringUtils.trim(rs.getString("Rubric")));
			String incipit = StringUtils.trim(rs.getString("Incipit"));
			incipit = StringUtils.isNotEmpty(incipit) ? incipit : StringUtils.trim(rs.getString("IncipitNS")); 
			content.setIncipit(incipit);
			String explicit = StringUtils.trim(rs.getString("Explicit"));
			explicit = StringUtils.isNotEmpty(explicit) ? explicit : StringUtils.trim(rs.getString("ExplicitNS"));
			content.setExplicit(explicit);			
			content.setItemNotes(StringUtils.trim(rs.getString("NotesToText")));
			content.setRevisit(rs.getBoolean("Revisit"));
			content.setItemNumber(rs.getInt("ItemNumber"));
			content.setAcknowledgments(StringUtils.trim(rs.getString("Acknowledgments")));
			content.setCatalogerTags(StringUtils.trim(rs.getString("Subject")));
			content.setTextBibliography(StringUtils.trim(rs.getString("TextBibliography")));
			
			List<Contributor> authors = findOrCreateContributor(
				StringUtils.trim(rs.getString("Author")), 
				StringUtils.trim(rs.getString("AuthorURI_LC")), 
				StringUtils.trim(rs.getString("AuthorURI_VIAF")), 
				StringUtils.trim(rs.getString("AuthorDisplay")));
			
			if(CollectionUtils.isNotEmpty(authors)) {
				for(Contributor author : authors) {
					contentContributors.add(new ContentContributor(content, author, Contributor.Type.AUTHOR, StringUtils.trim(rs.getString("AuthorNS"))));
				}				
			}
			
			List<Contributor> otherContributors = findOrCreateContributor(
				StringUtils.trim(rs.getString("AssociatedName")), 
				StringUtils.trim(rs.getString("AssociatedNameURI_LC")), 
				StringUtils.trim(rs.getString("AssociatedNameURI_VIAF")), 
				StringUtils.trim(rs.getString("AssociatedNameDisplay")));
			
			if(CollectionUtils.isNotEmpty(otherContributors)) {
				for(Contributor contributor : otherContributors) {
					contentContributors.add(new ContentContributor(content, contributor, Contributor.Type.OTHER, null));
				}				
			}
			
			List<Language> languages = findOrCreateLanguage(StringUtils.trim(rs.getString("Language(s)")));
			
			if(CollectionUtils.isNotEmpty(languages)) {				
				for(Language language : languages) {					
					contentLanguages.add(new ContentLanguage(contentId, language.getId()));
				}
				
				content.setLanguages(languages);
			}
						
			contents.add(content);
			contentForPart.add(content);
			contentByPartId.put(partId, contentForPart);
		}
		
		importResult.setContentsFromSource(contents.size());
		readingRoomService.bulkInsertReadingRoomContent(contents);
		readingRoomService.bulkInsertReadingRoomContentLanguages(contentLanguages);
		readingRoomService.bulkInsertReadingRoomContentContributors(contentContributors);		
		
		return contentByPartId;
	}
	
	private Map<Long, List<Image>> importReadingRoomImages(ResultSet rs, Import importResult) throws Exception {
		Map<Long, List<Image>> imagesByContentId = new HashMap<>();
		List<Image> images = new ArrayList<>();
	    
		while(rs.next()) {
			Image image = new Image();			
			image.setId(rs.getLong("LinkID"));
			Long contentId = rs.getLong("EDocID");
			List<Image> imagesForContent = imagesByContentId.get(contentId);
			
			if(imagesForContent == null) {
				imagesForContent = new ArrayList<Image>();
			}
			
			image.setParentContent(new Content(contentId));	
			image.setImgId(rs.getInt("EDocID")); 
			image.setFolioNumber(StringUtils.trim(rs.getString("FolioNumber")));
			image.setCaption(StringUtils.trim(rs.getString("Caption")));
			image.setNotesToPhotographer(StringUtils.trim(rs.getString("NotesToPhotographer")));
			image.setIconClass(StringUtils.trim(rs.getString("Iconclass")));
			image.setRevisit(rs.getBoolean("Revisit"));
			image.setSeq(rs.getInt("Seq"));
			images.add(image);
			imagesForContent.add(image);
			imagesByContentId.put(contentId, imagesForContent);
		}
		
		importResult.setImagesFromSource(images.size());
		readingRoomService.bulkInsertReadingRoomImage(images);
		
		return imagesByContentId;
	}
	
	private Country findOrCreateCountry(String countryName, String authUriLc, String authUriViaf, String nameIso3166) {
		
		Country country = null;
		
		if(StringUtils.isNotEmpty(countryName)) {
			// names have question marks in parenthesis to indicate if country is unknown, remove them
			countryName = StringUtils.remove(countryName, "(?)").trim();
			country = countryCache.get(countryName);
			
			if(country == null && StringUtils.isNotEmpty(countryName)) {
				
				country = countryRepository.findByName(countryName);
				
				if(country == null) {
					country = countryRepository.save(new Country(countryName, authUriLc, authUriViaf));
				}
				
				countryCache.put(countryName, country);
			}
		}
		
		return country;
	}
	
	private City findOrCreateCity(Long countryId, String cityName, String authUriLc, String authUriViaf) {
		City city = null;
		
		if(StringUtils.isNotEmpty(cityName)) {
			cityName = StringUtils.remove(cityName, "(?)").trim();	
			City newCity = new City(cityName, authUriLc, authUriViaf, countryId);
			city = cityCache.get(newCity);
			
			if(city == null) {
				city = cityRepository.findByNameAndCountryId(cityName, countryId);
				
				if(city == null) {
					city = cityRepository.save(newCity);
				}
				
				cityCache.put(city, city);
			}
		}		
		
		return city;
	}
	
	private Repository findOrCreateRepo(Long cityId, String repoName, String authUriLc, String authUriViaf) {
		
		Repository repo = null;
		
		if(StringUtils.isNotEmpty(repoName)) {
			repoName = repoName.trim();
			Repository newRepo = new Repository(repoName, authUriLc, authUriViaf,cityId);
			repo = repoCache.get(newRepo);
			
			if(repo == null) {
				repo = repoRepository.findByNameAndCityId(repoName, cityId);
				
				if(repo == null) {
					repo = repoRepository.save(newRepo);
				}
				
				repoCache.put(repo, repo);
			}
		}		
		
		return repo;		
	}
	
	private List<Language> findOrCreateLanguage(String languageNamesString) {
		List<Language> languages = new ArrayList<Language>();
		
		if(StringUtils.isNotEmpty(languageNamesString)) {
			String[] languageNames = languageNamesString.split(";");
			
			for(String languageName : languageNames) {
				languageName = languageName.trim();
				Language language = languageCache.get(languageName);
				
				if(language == null && StringUtils.isNotEmpty(languageName)) {
					
					language = languageRepository.findByName(languageName);
					
					if(language == null) {
						language = languageRepository.save(new Language(languageName));
					}
					
					languageCache.put(languageName, language);
				}
				
				languages.add(language);
			}
		}				
		
		return languages;
	}
	
	private List<WritingSystem> findOrCreateWritingSystem(String writingSystemString) {
		List<WritingSystem> writingSystems = new ArrayList<WritingSystem>();
		
		if(StringUtils.isNotEmpty(writingSystemString)) {
			String[] writingSystemNames = writingSystemString.split(";");
			
			for(String writingSystemName : writingSystemNames) {
				writingSystemName = writingSystemName.trim();
				WritingSystem writingSystem = writingSystemCache.get(writingSystemName);
				
				if(writingSystem == null && StringUtils.isNotEmpty(writingSystemName)) {
					
					writingSystem = writingSystemRepository.findByName(writingSystemName);
					
					if(writingSystem == null) {
						writingSystem = writingSystemRepository.save(new WritingSystem(writingSystemName));
					}
					
					writingSystemCache.put(writingSystemName, writingSystem);
				}
				
				writingSystems.add(writingSystem);
			}
		}				
		
		return writingSystems;
	}
	
	private List<Contributor> findOrCreateContributor(String contributorNamesString, String authorityUriLC, String authorityUriVIAF, String displayName) throws IOException {
		List<Contributor> contributors = new ArrayList<Contributor>();
		
		if(StringUtils.isNoneEmpty(contributorNamesString)) {
			String[] contributorNames = contributorNamesString.split(";");
			
			for(String contributorName : contributorNames) {
				contributorName = contributorName.trim();
				Contributor contributor = contributorCache.get(contributorName);
				
				if(contributor == null && StringUtils.isNotEmpty(contributorName)) {
					
					contributor = contributorRepository.findByName(contributorName);
					
					if(contributor == null) {
						contributor = contributorRepository.save(new Contributor(contributorName, authorityUriLC, authorityUriVIAF, displayName));
						elasticSearchService.updateIndexedObject(IndexedType.READING_ROOM_CONTRIBUTOR, contributor.getId().toString(), objectMapper.writeValueAsString(contributor));
					}
					
					contributorCache.put(contributorName, contributor);					
				}
				
				contributors.add(contributor);
			}
		}		
				
		return contributors;
	}
	
	private Genre findOrCreateGenre(String genreName, String uri) {
		
		Genre genre = null;
		
		if(StringUtils.isNotEmpty(genreName)) {
			genreName = genreName.trim();
			genre = genreCache.get(genreName);
			
			if(genre == null && StringUtils.isNotEmpty(genreName)) {
				
				genre = genreRepository.findByName(genreName);
				
				if(genre == null) {
					Genre newGenre = new Genre(genreName);
					newGenre.setUri(uri);
					genre = genreRepository.save(newGenre);
				}
				
				genreCache.put(genreName, genre);
			}
		}
		
		return genre;
	}
	
	private Institution findOrCreateInstitution(String institutionName) {
		
		Institution institution = null;
		
		if(StringUtils.isNotEmpty(institutionName)) {
			institutionName = institutionName.trim();
			institution = institutionCache.get(institutionName);
			
			if(institution == null && StringUtils.isNotEmpty(institutionName)) {
				
				institution = institutionRepository.findByName(institutionName);
				
				if(institution == null) {
					institution = institutionRepository.save(new Institution(institutionName));
				}
				
				institutionCache.put(institutionName, institution);
			}
		}
		
		return institution;
	}
	
	private UniformTitle findOrCreateUniformTitle(String name, String authorityUriLC, String authorityUriVIAF) {
		UniformTitle uniformTitle = null;
		
		if(StringUtils.isNotEmpty(name)) {
			name = name.trim();
			uniformTitle = uniformTitleCache.get(name);
			
			if(uniformTitle == null && StringUtils.isNotEmpty(name)) {
				
				uniformTitle = uniformTitleRepository.findByName(name);
				
				if(uniformTitle == null) {
					UniformTitle newUniformTitle = new UniformTitle(name, authorityUriLC, authorityUriVIAF);
					uniformTitle = uniformTitleRepository.save(newUniformTitle);
				}
				
				uniformTitleCache.put(name, uniformTitle);
			}
		}
		
		return uniformTitle;
	}
	
	private List<Subject> findOrCreateSubject(String namesString, String authorityUriLC, String authorityUriVIAF) {
		List<Subject> subjects = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(namesString)) {			
			String[] names = namesString.split(";");
			
			for(String name : names) {
				name = name.trim();
				Subject subject = subjectCache.get(name);
				
				if(subject == null && StringUtils.isNotEmpty(name)) {
					
					subject = subjectRepository.findByName(name);
					
					if(subject == null) {
						Subject newSubject = new Subject(name, authorityUriLC, authorityUriVIAF);
						subject = subjectRepository.save(newSubject);
					}
					
					subjectCache.put(name, subject);					
				}
				
				subjects.add(subject);
			}			
		}
		
		return subjects;
	}
	
	private void setWritingSpace(ReadingRoomObjectPart part, String writingSpaceString) {		
		if(StringUtils.isNotEmpty(writingSpaceString)) {
			writingSpaceString = writingSpaceString.trim();
			part.setWritingSpaceImported(writingSpaceString);
			Map<String, Float> dimensions = getDimensions(writingSpaceString);
			part.setWritingSpaceHeight(dimensions.get("height"));
			part.setWritingSpaceWidth(dimensions.get("width"));						
		}
	}
	
	private void setSupportDimensions(ReadingRoomObjectPart part, String supportDimensionsString) {		
		if(StringUtils.isNotEmpty(supportDimensionsString)) {
			supportDimensionsString = supportDimensionsString.trim();
			part.setSupportDimensionsImported(supportDimensionsString);
			Map<String, Float> dimensions = getDimensions(supportDimensionsString);
			part.setSupportDimensionsHeight(dimensions.get("height"));
			part.setSupportDimensionsWidth(dimensions.get("width"));						
		}
	}
	
	private void setBindingDimensions(ReadingRoomObject object, String bindingDimensionsString) {
		
		if(StringUtils.isNotEmpty(bindingDimensionsString)) {
			bindingDimensionsString = bindingDimensionsString.trim();
			object.setBindingDimensionsImported(bindingDimensionsString);
			Map<String, Float> dimensions = getDimensions(bindingDimensionsString);
			object.setBindingHeight(dimensions.get("height"));
			object.setBindingWidth(dimensions.get("width"));
			object.setBindingDepth(dimensions.get("depth"));						
		}
	}
	
	private Map<String, Float> getDimensions(String dimensionsString) {
		Map<String, Float> dimensions = new HashMap<>();
		
		if(StringUtils.isNotEmpty(dimensionsString)) {
			dimensionsString = dimensionsString.trim();			
			dimensionsString = StringUtils.removeEnd(dimensionsString, "cm");			
			dimensionsString = StringUtils.removeEnd(dimensionsString, "cm."); // sometimes the catalogers put a period on the end			
			String[] dimensionList = dimensionsString.split("[Xx×]"); // various forms of x are used by the catalogers, this regex says "any one of these"
			
			for(int i = 0; i < dimensionList.length; i++) {
				String dimension = dimensionList[i].trim();
				
				if(NumberUtils.isNumber(dimension)) {					
					if(i == 0) {
						dimensions.put("height", Float.parseFloat(dimension));
					} else if(i == 1) {
						dimensions.put("width", Float.parseFloat(dimension));						
					} else {
						dimensions.put("depth", Float.parseFloat(dimension));
					}
				}
			}						
		}
		
		return dimensions;
	}
	
	private String getValidSupportString(String supportString) {
		String validSupportString = null;
		
		if(StringUtils.isNotEmpty(supportString)) {
			String[] supportList = supportString.split(";"); 
			List<String> validSupports = new ArrayList<>();
			
			for(String support : supportList) {
				
				try {
					Support validSupport = Support.forName(support.trim());
					if(validSupport != null) {
						validSupports.add(validSupport.name());
					}
				} catch(IllegalArgumentException e) {
					LOG.warn("Invalid support name on import", e);
				}											
			}
			
			if(CollectionUtils.isNotEmpty(validSupports)) {
				validSupportString = AttributeUtil.getListAttributeAsString(validSupports);
			}
		}
				
		return validSupportString;
	}
	
	private void addNonNullToList(List<String> list, String value) {
		if(StringUtils.isNoneEmpty(value)) {
			list.add(value);
		}
	}
	
	private void clearCache() {
		countryCache = new HashMap<String, Country>();	
		cityCache = new HashMap<City, City>();
		repoCache = new HashMap<Repository, Repository>();
		languageCache = new HashMap<String, Language>();
		contributorCache = new HashMap<String, Contributor>();
		genreCache = new HashMap<String, Genre>();
		institutionCache = new HashMap<String, Institution>();
		uniformTitleCache = new HashMap<String, UniformTitle>();
		featureCache = new HashMap<String, Feature>();		
		subjectCache = new HashMap<String, Subject>();		
	}
	
	private ArchivalData getArchivalData(ResultSet rs) throws SQLException, IOException {
		Long id = rs.getLong("MsID");
		ArchivalData archivalData = new ArchivalData(id);
		ArchivalObject parentObject = new ArchivalObject(id);
		
		archivalData.setParentObject(parentObject);
		archivalData.setCollectionFond(StringUtils.trim(rs.getString("ArcCollectionFond")));
		archivalData.setSeries(StringUtils.trim(rs.getString("ArcArchivalSeries")));
		archivalData.setSubSeries(StringUtils.trim(rs.getString("ArcArchivalSubseries")));
		archivalData.setSubSubSeries(StringUtils.trim(rs.getString("ArcArchivalSubsubseries")));
		archivalData.setContainerCount(rs.getInt("ArcContainer"));
		archivalData.setContainerType(StringUtils.trim(rs.getString("ArcContainerType")));		
		archivalData.setCreator(StringUtils.trim(rs.getString("ArcCreator")));
		archivalData.setTitle(StringUtils.trim(rs.getString("ArcTitle")));
		archivalData.setScopeContent(StringUtils.trim(rs.getString("ArcScopeContent")));
		archivalData.setHistoricalNote(StringUtils.trim(rs.getString("ArcHistoricalNotes")));
		archivalData.setCustodialHistory(StringUtils.trim(rs.getString("ArcCustodialHistory")));
		archivalData.setCenturyImported(StringUtils.trim(rs.getString("ArcDateCentury")));
		archivalData.setNativeDate(StringUtils.trim(rs.getString("ArcNativeDate")));		
		archivalData.setDecoration(StringUtils.trim(rs.getString("DecorationIllustration")));
		String yearString = StringUtils.trim(rs.getString("ArcYear"));
		String monthString = StringUtils.trim(rs.getString("ArcMonth"));
		String dayString = StringUtils.trim(rs.getString("ArcDay"));
		String beginDateString = StringUtils.trim(rs.getString("ArcBeginDate"));
		String endDateString = StringUtils.trim(rs.getString("ArcEndDate"));
		
		if(StringUtils.isNotEmpty(yearString)) {
			archivalData.setDatePreciseYear(Integer.valueOf(yearString));
		}
		
		if(StringUtils.isNotEmpty(monthString)) {
			archivalData.setDatePreciseMonth(Integer.valueOf(monthString) - 1);
		}
		
		if(StringUtils.isNotEmpty(dayString)) {
			archivalData.setDatePreciseDay(Integer.valueOf(dayString));
		}
		
		if(StringUtils.isNotEmpty(beginDateString)) {
			archivalData.setBeginDate(Integer.valueOf(beginDateString));
		}
		
		if(StringUtils.isNotEmpty(endDateString)) {
			archivalData.setEndDate(Integer.valueOf(endDateString));
		}
		
		archivalData.setSupport(getSupport(rs));
		String writingSystemString = StringUtils.trim(rs.getString("ArcWritingSystem"));
		// writing systems are store in writing system table because catalogers can add new ones
		
		if(StringUtils.isNotEmpty(writingSystemString)) {
			findOrCreateWritingSystem(writingSystemString);
			archivalData.setWritingSystem(writingSystemString.replaceAll(";", ","));
		}		
		
		return archivalData;
	}
	
	private String getSupport(ResultSet rs) throws SQLException {
		StringBuilder enumString = new StringBuilder();		
		String supportString = StringUtils.trim(rs.getString("ArcSupport"));
		
		if(StringUtils.isNotEmpty(supportString)) {
			// we store the list of supports comma delimited but they might be semi colon delimited coming from Oliver
			List<String> supports = AttributeUtil.getStringAttributeAsList(supportString.replaceAll(";", ","));
			
			// need to convert display names used in Oliver to typed enum values
			for(int i = 0; i < supports.size(); i++) {				
				if(i > 0) {
					enumString.append(", ");
				}
				
				enumString.append(Support.forName(supports.get(i)));
			}			
		}
		
		return enumString.toString();
	}
	
	private List<ReadingRoomObjectContributor> getFormerOwners(ResultSet rs) throws SQLException, IOException {
		List<ReadingRoomObjectContributor> formerOwners = new ArrayList<>();
		String formerOwnerName = rs.getString("FormerOwner");
		
		if(StringUtils.isNotEmpty(formerOwnerName)) {
			List<Contributor> contributors = findOrCreateContributor(formerOwnerName, null, null, null);
			Long id = rs.getLong("MsID");
			
			for(int i = 0; i < contributors.size(); i++) {
				ReadingRoomObject parentObject = new ReadingRoomObject(id);
				ReadingRoomObjectContributor objectContributor = new ReadingRoomObjectContributor();
				objectContributor.setContributor(contributors.get(i));
				objectContributor.setReadingRoomObject(parentObject);	
				objectContributor.setType(Contributor.Type.FORMER_OWNER);
				formerOwners.add(objectContributor);
			}
		}				
		
		return formerOwners;
	}
	
	private List<ArchivalContributor> getArchivalContributors(ResultSet rs) throws SQLException, IOException {
		List<ArchivalContributor> archivalContributors = new ArrayList<>();
		Long id = rs.getLong("MsID");
		ArchivalData parentArchivalData = new ArchivalData(id);
		List<Contributor> contributors = findOrCreateContributor(
				StringUtils.trim(rs.getString("ArcAssociatedName")), 
				StringUtils.trim(rs.getString("AssociatedNamesURI_LC")), 
				StringUtils.trim(rs.getString("AssociatedNamesURI_VIAF")), 
				StringUtils.trim(rs.getString("ArcAssociatedNameDisplay")));
		
		String associatedNameType = rs.getString("ArcAssociatedNameValue");
		String associatedNameNs = rs.getString("ArcAssociatedNameNS");
		String[] associatedNameTypes = StringUtils.isNotEmpty(associatedNameType) ? StringUtils.trim(associatedNameType).split(";") : new String[] {Contributor.Type.OTHER.getDisplayName()};
		String[] associatedNamesNs = StringUtils.isNotEmpty(associatedNameNs) ? StringUtils.trim(associatedNameNs).split(";") : new String[] {""};
		
		for(int i = 0; i < contributors.size(); i++) {
			String contributorType = associatedNameTypes.length == contributors.size() ? associatedNameTypes[i] : associatedNameTypes[0];
			String nameNs = associatedNamesNs.length == contributors.size() ? associatedNamesNs[i] : associatedNamesNs[0];
			ArchivalContributor archivalContributor = new ArchivalContributor();
			archivalContributor.setContributor(contributors.get(i));
			archivalContributor.setNameNs(nameNs);
			archivalContributor.setParentArchivalData(parentArchivalData);			
			
			try {
				Contributor.Type type = Contributor.Type.forDisplayName(contributorType);
				archivalContributor.setType(type);
			} catch(IllegalArgumentException e) {
				LOG.error("Invalid contributor type on archival record being imported with id " + id + ", default contributor type of \"Other\" will be used.", e);
				archivalContributor.setType(Contributor.Type.OTHER);
			}
			
			archivalContributors.add(archivalContributor);
		}
		
		return archivalContributors;
	}
	
	private List<ArchivalDataLanguage> getArchivalDataLanguages(ResultSet rs) throws SQLException {
		Long id = rs.getLong("MsID");
		List<ArchivalDataLanguage> archivalDataLanguages = new ArrayList<>();
		List<Language> languages = findOrCreateLanguage(StringUtils.trim(rs.getString("ArcLanguage")));
		
		if(CollectionUtils.isNotEmpty(languages)) {				
			for(Language language : languages) {					
				archivalDataLanguages.add(new ArchivalDataLanguage(id, language.getId()));
			}			
		}
		
		return archivalDataLanguages;
	}
}
