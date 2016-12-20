package org.vhmml.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.vhmml.dto.AuthorityListSearchResult;
import org.vhmml.dto.TitleSearchResult;
import org.vhmml.entity.NamedIdentifiable;
import org.vhmml.entity.readingroom.ArchivalContainer;
import org.vhmml.entity.readingroom.City;
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
import org.vhmml.entity.readingroom.WritingSystem;
import org.vhmml.exception.ExistingReferencesException;
import org.vhmml.repository.readingroom.NamedIdentifiableRepository;

public interface AuthorityListService {
	
	enum AuthorityListType {
		CITY("City"),
		CONTAINER("Container"),
		CONTRIBUTOR("Name"),
		COUNTRY("Country"), 
		FEATURE("Feature"),
		FORMAT("Format"), 
		GENRE("Genre/Form"),
		INSTITUTION("Institution"), 
		LANGUAGE("Language"),
		REPOSITORY("Repository"), 
		SCRIPT("Script"), 
		TITLE("Title"), 
		UNIFORM_TITLE("Uniform Title"),
		WRITING_SYSTEM("Writing System");

		private String displayName;

		AuthorityListType(String displayName) {
			this.displayName = displayName;
		}

		public String getDisplayName() {
			return displayName;
		}
	}

	public Object addListOption(AuthorityListType type, String name, String authorityUrlLC, String authorityUrlVIAF, Long parentOptionId);
	public List<Contributor> searchContributors(String startsWith, Pageable pageable);
	public List<Repository> getAllRepositories();
	public List<Repository> getRepositoriesByCity(Long cityId);
	public List<Institution> getAllInstitutions();
	public List<Format> getAllFormats();
	public List<Feature> getAllFeatures();
	public List<City> getAllCities();
	public List<City> getCitiesByCountry(Long countryId);
	public List<Country> getAllCountries();
	public List<Language> getAllLanguages();
	public List<Genre> getAllGenres();
	public List<Script> getAllScripts();
	public List<WritingSystem> getAllWritingSystems();
	public List<Subject> getAllSubjects();
	public List<Contributor> getAllContributors();
	public List<ArchivalContainer> getAllArchivalContainers();
	public <T extends NamedIdentifiable> T find(AuthorityListService.AuthorityListType authListType, Long objectId);
	public AuthorityListSearchResult searchAuthorityList(AuthorityListService.AuthorityListType authListType, String searchText, Pageable pageable);
	public TitleSearchResult searchContentTitles(String searchString, Pageable pageable);
	public List<ReadingRoomObject> getReferences(AuthorityListService.AuthorityListType authListType, Long objectId);
	public void delete(AuthorityListService.AuthorityListType authListType, Long objectId) throws ExistingReferencesException;
	public <T extends NamedIdentifiable> T findOrCreateNamedEntity(T entity, NamedIdentifiableRepository<T> repository);	
	public <T extends NamedIdentifiable> List<T> findOrCreateNamedEntities(List<T> namedEntities, NamedIdentifiableRepository<T> repository);
	public <T extends NamedIdentifiable> T save(T entity, AuthorityListService.AuthorityListType authListType);
	public ContentTitleView saveContentTitle(Long contentId, String title);
}
