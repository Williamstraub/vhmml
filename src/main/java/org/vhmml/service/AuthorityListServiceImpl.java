package org.vhmml.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.vhmml.dto.AuthorityListSearchResult;
import org.vhmml.dto.TitleSearchResult;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.NamedIdentifiable;
import org.vhmml.entity.readingroom.ArchivalContainer;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentTitleView;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Country;
import org.vhmml.entity.readingroom.Feature;
import org.vhmml.entity.readingroom.Format;
import org.vhmml.entity.readingroom.Genre;
import org.vhmml.entity.readingroom.Institution;
import org.vhmml.entity.readingroom.Language;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.entity.readingroom.Script;
import org.vhmml.entity.readingroom.Subject;
import org.vhmml.entity.readingroom.UniformTitle;
import org.vhmml.entity.readingroom.WritingSystem;
import org.vhmml.exception.ExistingReferencesException;
import org.vhmml.repository.CityRepository;
import org.vhmml.repository.CountryRepository;
import org.vhmml.repository.readingroom.ArchivalContainterRepository;
import org.vhmml.repository.readingroom.ContentTitleViewRepository;
import org.vhmml.repository.readingroom.NamedIdentifiableRepository;
import org.vhmml.repository.readingroom.ReadingRoomContentRepository;
import org.vhmml.repository.readingroom.ReadingRoomContributorRepository;
import org.vhmml.repository.readingroom.ReadingRoomFeatureRepository;
import org.vhmml.repository.readingroom.ReadingRoomFormatRepository;
import org.vhmml.repository.readingroom.ReadingRoomGenreRepository;
import org.vhmml.repository.readingroom.ReadingRoomInstitutionRepository;
import org.vhmml.repository.readingroom.ReadingRoomLanguageRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomSubjectRepository;
import org.vhmml.repository.readingroom.RepositoryRepository;
import org.vhmml.repository.readingroom.ScriptRepository;
import org.vhmml.repository.readingroom.UniformTitleRepository;
import org.vhmml.repository.readingroom.WritingSystemRepository;

import com.google.common.collect.Lists;

@Service
public class AuthorityListServiceImpl implements AuthorityListService {

	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private ReadingRoomFeatureRepository featureRepository;
	
	@Autowired
	private ReadingRoomInstitutionRepository institutionRepository;
	
	@Autowired
	private RepositoryRepository repositoryRepo;
	
	@Autowired
	private ScriptRepository scriptRepository;
	
	@Autowired
	private WritingSystemRepository writingSystemRepository;
	
	@Autowired
	private ArchivalContainterRepository archivalContainerRepository;
	
	@Autowired
	private ReadingRoomFormatRepository formatRepository;
	
	@Autowired
	private ReadingRoomLanguageRepository languageRepository;
	
	@Autowired
	private ReadingRoomGenreRepository genreRepository;
	
	@Autowired
	private ReadingRoomSubjectRepository subjectRepository;
	
	@Autowired
	private ReadingRoomContributorRepository contributorRepository;
	
	@Autowired
	private ReadingRoomObjectRepository readingRoomObjectRepository;
	
	@Autowired
	private UniformTitleRepository uniformTitleRepository;
	
	@Autowired
	private ReadingRoomContentRepository contentRepository;
	
	@Autowired
	private ContentTitleViewRepository contentTitleViewRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	public Object addListOption(AuthorityListService.AuthorityListType type, String name, String authorityUrlLC, String authorityUrlVIAF, Long parentOptionId) {
		Object newOption = null;
		
		switch(type) {
			case CITY:
				newOption = cityRepository.save(new City(name, authorityUrlLC, authorityUrlVIAF, parentOptionId));
				break;
			case CONTAINER:
				newOption = archivalContainerRepository.save(new ArchivalContainer(name));
				break;
			case CONTRIBUTOR:
				newOption = contributorRepository.save(new Contributor(name, authorityUrlLC, authorityUrlVIAF));
				break;
			case COUNTRY:
				newOption = countryRepository.save(new Country(name, authorityUrlLC, authorityUrlVIAF));
				break;			
			case FEATURE:
				newOption = featureRepository.save(new Feature(name));
				break;
			case FORMAT:
				newOption = formatRepository.save(new Format(name));
				break;
			case GENRE:			
				newOption = genreRepository.save(new Genre(name));
				break;
			case INSTITUTION:
				newOption = institutionRepository.save(new Institution(name));
				break;
			case LANGUAGE:
				newOption = languageRepository.save(new Language(name));
				break;						
			case REPOSITORY:
				newOption = repositoryRepo.save(new Repository(name, authorityUrlLC, authorityUrlVIAF, parentOptionId));
				break;
			case SCRIPT:
				newOption = scriptRepository.save(new Script(name));
				break;
			case UNIFORM_TITLE:
				newOption = uniformTitleRepository.save(new UniformTitle(name, authorityUrlVIAF, authorityUrlVIAF));
				break;
			case WRITING_SYSTEM:
				newOption = writingSystemRepository.save(new WritingSystem(name));
				break;
			default:
				break;			
		}
		
		return newOption;
	}		
	
	public List<Contributor> searchContributors(String startsWith, Pageable pageable) {
		List<Contributor> contributors = new ArrayList<>();
		SearchResponse response = elasticSearchService.phrasePrefixSearch(				
			IndexedType.READING_ROOM_CONTRIBUTOR,
			startsWith,
			pageable,
			new String[] {"name", "displayName", "nameNs", "authorityUriLc", "authorityUriViaf", "name.folded", "displayName.folded", "nameNs.folded", "authorityUriLc.folded", "authorityUriViaf.folded"});		
		SearchHit[] hits = response.getHits().getHits();        
        
        for(SearchHit hit : hits) {
        	Contributor contributor = new Contributor(); 
        	Map<String, Object> source = hit.getSource();
        	contributor.setName((String)source.get("name"));
        	contributor.setDisplayName((String)source.get("displayName"));
        	contributor.setAuthorityUriLC((String)source.get("authorityUriLC"));
        	contributor.setAuthorityUriVIAF((String)source.get("authorityUriVIAF"));
        	contributors.add(contributor);
        }        	
		
		return contributors;
	}
	
	public List<Repository> getAllRepositories() {
		List<Repository> repositories = repositoryRepo.findAllByOrderByNameAsc();
		Map<String, Repository> uniqueRepositorysByName = new LinkedHashMap<>();
		
		for(Repository repo : repositories) {
			uniqueRepositorysByName.put(repo.getName(), repo);
		}
		
		repositories = Lists.newArrayList(uniqueRepositorysByName.values());
		
		return repositories;
	}
	
	public List<Repository> getRepositoriesByCity(Long cityId) {
		return Lists.newArrayList(repositoryRepo.findByCityIdOrderByNameAsc(cityId));
	}
	
	public List<Institution> getAllInstitutions() {
		return institutionRepository.findAllByOrderByNameAsc();
	}
	
	public List<Format> getAllFormats() {
		return Lists.newArrayList(formatRepository.findAll());
	}	
	
	public List<Feature> getAllFeatures() {
		return Lists.newArrayList(featureRepository.findAllByOrderByNameAsc());
	}
	
	public List<City> getAllCities() {
		return Lists.newArrayList(cityRepository.findAllByOrderByNameAsc());
	}
	
	public List<City> getCitiesByCountry(Long countryId) {
		return Lists.newArrayList(cityRepository.findByCountryIdOrderByNameAsc(countryId));
	}
	
	public List<Country> getAllCountries() {
		return Lists.newArrayList(countryRepository.findAllByOrderByNameAsc());
	}		
	
	public List<Language> getAllLanguages() {
		return Lists.newArrayList(languageRepository.findAllByOrderByNameAsc());
	}
	
	public List<Genre> getAllGenres() {
		return Lists.newArrayList(genreRepository.findAllByOrderByNameAsc());
	}	
	
	public List<Script> getAllScripts() {
		return Lists.newArrayList(scriptRepository.findAllByOrderByNameAsc());
	}
	
	public List<WritingSystem> getAllWritingSystems() {
		return Lists.newArrayList(writingSystemRepository.findAllByOrderByNameAsc());
	}
	
	public List<Subject> getAllSubjects() {
		return Lists.newArrayList(subjectRepository.findAllByOrderByNameAsc());
	}
	
	public List<ArchivalContainer> getAllArchivalContainers() {
		return Lists.newArrayList(archivalContainerRepository.findAllByOrderByNameAsc());
	}
	
	public List<Contributor> getAllContributors() {
		return contributorRepository.findAllByOrderByNameAsc();
	}
	
	public void delete(AuthorityListService.AuthorityListType authListType, Long objectId) throws ExistingReferencesException {
		
		List<ReadingRoomObject> existingReferences = getReferences(authListType, objectId);
		
		if(CollectionUtils.isNotEmpty(existingReferences)) {
			throw new ExistingReferencesException("Unable to delete " + authListType.getDisplayName().toLowerCase() + " because it is references by existing objects in Reading Room.", existingReferences);
		} else {
			switch(authListType) {
				case COUNTRY:
					countryRepository.delete(objectId);
					break;
				case CITY:
					cityRepository.delete(objectId);
					break;
				case REPOSITORY:
					repositoryRepo.delete(objectId);
					break;
				case FEATURE:
					featureRepository.delete(objectId);
					break;
				case GENRE:
					genreRepository.delete(objectId);
					break;
				case CONTAINER:
					archivalContainerRepository.delete(objectId);
					break;
				case FORMAT:
					formatRepository.delete(objectId);
					break;
				case LANGUAGE:
					languageRepository.delete(objectId);
					break;
				case INSTITUTION:
					institutionRepository.delete(objectId);
					break;
				case UNIFORM_TITLE:
					uniformTitleRepository.delete(objectId);
					break;
				case CONTRIBUTOR:
					contributorRepository.delete(objectId);
					break;
				default:
					break;
			}
		}		
	}
	
	public TitleSearchResult searchContentTitles(String searchString, Pageable pageable) {
		Page<ContentTitleView> titles;

		if(StringUtils.isEmpty(searchString)) {		
			titles = contentTitleViewRepository.findAll(pageable);
		} else {
			titles = contentTitleViewRepository.findByTitleContains(searchString, pageable);
		}
		
		return new TitleSearchResult(titles);
	}
	
	public ContentTitleView saveContentTitle(Long contentId, String title) {
		Content content = contentRepository.findOne(contentId);
		
		if(content != null) {
			content.setProvisionalTitle(title);
			contentRepository.save(content);
		}
		
		return contentTitleViewRepository.findOne(contentId);
	}
	
	public AuthorityListSearchResult searchAuthorityList(AuthorityListService.AuthorityListType authListType, String searchString, Pageable pageable) {
		Page<? extends NamedIdentifiable> authorityListObjects = null;
		NamedIdentifiableRepository<? extends NamedIdentifiable> repo = getRepositoryForType(authListType);
		
		if(StringUtils.isEmpty(searchString)) {
			authorityListObjects = repo.findAll(pageable);
		} else {
			authorityListObjects = repo.findByNameContains(searchString, pageable);
		}
		
		return new AuthorityListSearchResult(authorityListObjects);
	}
	
	public <T extends NamedIdentifiable> T find(AuthorityListService.AuthorityListType authListType, Long objectId) {
		NamedIdentifiableRepository<T> repo = getRepositoryForType(authListType);
		
		return repo.findOne(objectId);				
	}
	
	@Transactional
	public <T extends NamedIdentifiable> T save(T entity, AuthorityListService.AuthorityListType authListType) {
		T dbEntity = find(authListType, entity.getId());
		NamedIdentifiableRepository<T> repo = getRepositoryForType(authListType);
		BeanUtils.copyProperties(entity, dbEntity);
		return repo.save(dbEntity);
	}
	
	public <T extends NamedIdentifiable> T findOrCreateNamedEntity(T entity, NamedIdentifiableRepository<T> repository) {
		T dbEntity = null;
		
		if(entity != null) {
			dbEntity = repository.findByName(entity.getName());
			
			if(dbEntity != null) {
				BeanUtils.copyProperties(entity, dbEntity, "id");			
			} else {
				dbEntity = repository.save(entity);					
			}
		}
		
		
		return dbEntity;
	}
	
	public <T extends NamedIdentifiable> List<T> findOrCreateNamedEntities(List<T> namedEntities, NamedIdentifiableRepository<T> repository) {
		List<T> dbEntities = new ArrayList<T>();
		
		if(CollectionUtils.isNotEmpty(namedEntities)) {
			for(T entity : namedEntities) {				
				dbEntities.add(findOrCreateNamedEntity(entity, repository));				
			}
		}
		
		return dbEntities;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends NamedIdentifiable> NamedIdentifiableRepository<T> getRepositoryForType(AuthorityListService.AuthorityListType authListType) {
		NamedIdentifiableRepository repo = null;
		
		switch(authListType) {
			case CONTAINER:
				repo = archivalContainerRepository;
				break;
			case CITY:
				repo = cityRepository;
				break;
			case CONTRIBUTOR:
				repo = contributorRepository;
				break;
			case COUNTRY:
				repo = countryRepository;				
				break;
			case FEATURE:
				repo = featureRepository;
				break;
			case FORMAT:
				repo = formatRepository;
				break;
			case GENRE:
				repo = genreRepository;
				break;
			case INSTITUTION:
				repo = institutionRepository;
				break;
			case LANGUAGE:
				repo = languageRepository;
				break;
			case REPOSITORY:
				repo = repositoryRepo;
				break;
			case SCRIPT:
				repo = scriptRepository;
				break;
			case UNIFORM_TITLE:
				repo = uniformTitleRepository;
				break;
			case WRITING_SYSTEM:
				repo = writingSystemRepository;
				break;
			default:
				break;
		}
		
		return repo;
	}
	
	public List<ReadingRoomObject> getReferences(AuthorityListService.AuthorityListType authListType, Long objectId) {
		List<ReadingRoomObject> references = new ArrayList<ReadingRoomObject>();
		
		switch(authListType) {
			case COUNTRY:
				references = readingRoomObjectRepository.findByCountryId(objectId);
				break;
			case CITY:
				references = readingRoomObjectRepository.findByCityId(objectId);
				break;
			case REPOSITORY:
				references = readingRoomObjectRepository.findByRepositoryId(objectId);
				break;
			case FEATURE:
				references = readingRoomObjectRepository.findByFeatureId(objectId);
				break;
			case GENRE:
				references = readingRoomObjectRepository.findByGenreId(objectId);
				break;
			case CONTAINER:
				references = readingRoomObjectRepository.findByArchivalContainerId(objectId);
				break;
			case FORMAT:
				references = readingRoomObjectRepository.findByFormatId(objectId);
				break;
			case LANGUAGE:
				references = readingRoomObjectRepository.findByLanguageId(objectId);
				break;
			case INSTITUTION:
				references = readingRoomObjectRepository.findByHoldingInstitutionId(objectId);
				break;
			case UNIFORM_TITLE:
				references = readingRoomObjectRepository.findByUniformTitleId(objectId);
				break;
			case CONTRIBUTOR:
				references = readingRoomObjectRepository.findByContributorId(objectId);
				break;
			default:
				break;
		}
		
		return references;
	}
}
