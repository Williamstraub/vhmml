package org.vhmml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vhmml.dto.VhmmlSearchParameters;
import org.vhmml.entity.Role;
import org.vhmml.entity.readingroom.ArchivalContent;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Font;
import org.vhmml.entity.readingroom.Genre;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.entity.readingroom.Subject;
import org.vhmml.entity.readingroom.Support;
import org.vhmml.service.AuthorityListService;
import org.vhmml.service.UserService;
import org.vhmml.web.VhmmlSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@Controller
public class BaseReadingRoomController extends BaseValidationController {

	private static final Logger LOG = Logger.getLogger(BaseReadingRoomController.class);
	
	public static final String PARAM_READING_ROOM_OBJECT = "readingRoomObject";
	public static final String PARAM_READING_ROOM_OBJECT_FORM = "readingRoomObjectForm";
	public static final String PARAM_READING_ROOM_OBJECT_FORM_JSON = "readingRoomObjectFormJson";
	public static final String PARAM_INSTITUTIONS = "institutions";
	public static final String PARAM_OBJECT_TYPES = "objectTypes";
	public static final String PARAM_EXTENT_TYPES = "extentTypes";
	public static final String PARAM_FORMATS = "formats";
	public static final String PARAM_FEATURES = "features";
	public static final String PARAM_COUNTRIES = "countries";
	public static final String PARAM_CITIES_BY_COUNTRY = "citiesByCountry";
	public static final String PARAM_CITIES_JSON = "citiesJson";
	public static final String PARAM_REPOSITORIES_BY_CITY = "repositoriesByCity";
	public static final String PARAM_REPOSITORIES_JSON = "repositoriesJson";
	public static final String PARAM_LANGUAGES = "languages";
	public static final String PARAM_SUPPORTS = "supports";
	public static final String PARAM_GENRES = "genres";
	public static final String PARAM_GENRES_JSON = "genresJson";
	public static final String PARAM_SUBJECTS = "subjects";
	public static final String PARAM_ARCHIVAL_CONTAINERS = "archivalContainers";
	public static final String PARAM_SUBJECTS_JSON = "subjectsJson";
	public static final String PARAM_STATUSES = "statuses";
	public static final String PARAM_IMPORTS = "imports";
	public static final String PARAM_ACCESS_RESTRICTIONS = "accessRestrictions";
	public static final String PARAM_DOWNLOAD_OPTIONS = "downloadOptions";
	public static final String PARAM_CENTURIES = "centuries";
	public static final String PARAM_WRITING_SYSTEMS = "writingSystems";
	public static final String PARAM_SCRIPTS = "scripts";
	public static final String PARAM_ASSOCIATED_NAME_TYPES = "associatedNameTypes";
	public static final String PARAM_PART_TYPES = "partTypes";
	public static final String PARAM_ARCHIVAL_ITEM_TYPES = "archivalItemTypes";
	public static final String PARAM_FONTS = "fonts";
	public static final String PARAM_ACCEPTED_AGREEMENT = "acceptedReadingRoomAgreement";
	public static final String PARAM_SCHOLARS = "scholars";
	
	public static final String ATT_SAVED_SEARCH = "savedSearch";
	public static final String ATT_SAVED_SEARCH_JSON = "savedSearchJson";
	
	@Autowired
	private AuthorityListService authorityListService;
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value = "/options/{listName}", method = RequestMethod.GET)
	public ResponseEntity<Object> getOptionList(@PathVariable String listName, HttpServletRequest request) {
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to retrieve " + listName + " list. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);		
		
		try {
			List<? extends Object> objects = null;
			String nameProperty = "name";
			String valueProperty = "name";
			
			switch(listName) {
				case "language":
					objects = authorityListService.getAllLanguages();
					break;
				case "country":
					objects = authorityListService.getAllCountries();
					valueProperty = "id";
					break;
				case "city":
					objects = authorityListService.getAllCities();
					valueProperty = "id";
					break;
				case "repository":
					objects = authorityListService.getAllRepositories();
					valueProperty = "id";
					break;
				case "objectType":
					objects = Lists.newArrayList(ReadingRoomObject.Type.values());
					nameProperty = "displayName";					
					break;
				case "genres":
					objects = authorityListService.getAllGenres();
					break;
				case "features":
					objects = authorityListService.getAllFeatures();
					break;
				case "script":
					objects = authorityListService.getAllScripts();
					break;
				case "writingSystem":
					objects = authorityListService.getAllWritingSystems();
					break;
			}
			
			List<Map<String, Object>> options = getOptions(objects, listName, nameProperty, valueProperty, request);
			response = new ResponseEntity<Object>(options, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Unable to retrieve language list", e);
		}

		return response;
	}
	
	private List<Map<String, Object>> getOptions(List<? extends Object> objects, String listName, String nameProperty, String valueProperty, HttpServletRequest request) {
		List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();		
		VhmmlSearchParameters savedSearch = VhmmlSession.getSession(request).getSavedReadingRoomSearch();
		
		try {
			for(Object object : objects) {
				Map<String, Object> option = new HashMap<String, Object>();
				String name = BeanUtils.getProperty(object, nameProperty);
				option.put("name", name);			
				option.put("value", BeanUtils.getProperty(object, valueProperty));
				
				if(savedSearch != null && savedSearch.contains(listName, name)) {
					option.put("selected", true);					
				}
				
				options.add(option);
			}
		} catch(Exception e) {
			LOG.error("Exception attempting to get option list for " + listName, e);
		}
		
		return options;
	}
	
	void addReadingRoomLists(Map<String, Object> map, HttpServletRequest request) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		map.put(PARAM_INSTITUTIONS, authorityListService.getAllInstitutions());
		map.put(PARAM_OBJECT_TYPES, ReadingRoomObject.Type.values());
		map.put(PARAM_STATUSES, ReadingRoomObject.Status.values());
		map.put(PARAM_EXTENT_TYPES, ReadingRoomObject.ExtentType.values());
		map.put(PARAM_FEATURES, authorityListService.getAllFeatures());
		map.put(PARAM_FORMATS, authorityListService.getAllFormats());
		map.put(PARAM_COUNTRIES, authorityListService.getAllCountries());
		map.put(PARAM_CITIES_JSON, objectMapper.writeValueAsString(authorityListService.getAllCities()));
		map.put(PARAM_REPOSITORIES_JSON, objectMapper.writeValueAsString(authorityListService.getAllRepositories()));
		map.put(PARAM_LANGUAGES, authorityListService.getAllLanguages());
		map.put(PARAM_SUPPORTS, Support.values());
		List<Genre> genres = authorityListService.getAllGenres();
		map.put(PARAM_GENRES, genres);
		map.put(PARAM_GENRES_JSON, objectMapper.writeValueAsString(genres));
		List<Subject> subjects = authorityListService.getAllSubjects();
		map.put(PARAM_SUBJECTS, subjects);
		map.put(PARAM_ARCHIVAL_CONTAINERS, authorityListService.getAllArchivalContainers());
		map.put(PARAM_SUBJECTS_JSON, objectMapper.writeValueAsString(subjects));
		map.put(PARAM_ACCESS_RESTRICTIONS, ReadingRoomObject.AccessRestriction.values());
		map.put(PARAM_DOWNLOAD_OPTIONS, ReadingRoomObject.DownloadOption.values());
		map.put(PARAM_CENTURIES, getCenturies());
		map.put(PARAM_WRITING_SYSTEMS, authorityListService.getAllWritingSystems());
		map.put(PARAM_SCRIPTS, authorityListService.getAllScripts());
		map.put(PARAM_ASSOCIATED_NAME_TYPES, Contributor.Type.getAssociatedNameTypes());
		map.put(PARAM_PART_TYPES, ReadingRoomObjectPart.Type.values());
		map.put(PARAM_ARCHIVAL_ITEM_TYPES, ArchivalContent.Type.values());
		map.put(PARAM_FONTS, Font.values());
		map.put(PARAM_ACCEPTED_AGREEMENT, VhmmlSession.getSession(request).isAcceptedReadingRoomAgreement());
		map.put(PARAM_SCHOLARS, userService.findByRole(Role.Name.ROLE_SCHOLAR));
		
		List<City> cities = authorityListService.getAllCities();
		Map<Long, List<City>> citiesByCountry = new HashMap<>();
		
		for(City city : cities) {
			List<City> citiesForCountry = citiesByCountry.get(city.getCountryId());
			
			if(citiesForCountry == null) {
				citiesForCountry = new ArrayList<City>();
			}
			
			citiesForCountry.add(city);
			citiesByCountry.put(city.getCountryId(), citiesForCountry);
		}
		
		map.put(PARAM_CITIES_BY_COUNTRY, citiesByCountry);
		
		List<Repository> repos = authorityListService.getAllRepositories();
		Map<Long, List<Repository>> reposByCity = new HashMap<>();
		
		for(Repository repo : repos) {
			List<Repository> reposForCity = reposByCity.get(repo.getCityId());
			
			if(reposForCity == null) {
				reposForCity = new ArrayList<Repository>();
			}
			
			reposForCity.add(repo);
			reposByCity.put(repo.getCityId(), reposForCity);
		}
		
		map.put(PARAM_REPOSITORIES_BY_CITY, reposByCity);
	}
	
	private Map<Integer, String> getCenturies() {
		Map<Integer, String> centuries = new TreeMap<>(new Comparator<Integer>() {
			// tree map stores values sorted by key, we want the centuries sorted in reverse order
			@Override
			public int compare(Integer key1, Integer key2) {				
				return key2.compareTo(key1);
			}	
		});

		for(int i = 21; i >= -25; i--) {
			if(i == 0) {
				i--;
			}
			String century = new Integer(Math.abs(i)).toString();
			century += 
				century.endsWith("1") && !century.endsWith("11") ? "st" : 
				century.endsWith("2") && !century.endsWith("12") ? "nd" : 
				century.endsWith("3") && !century.endsWith("13") ? "rd" : "th";
			century += i < 0 ? " BCE" : " CE";
			centuries.put(i, century);			
		}		
		
		return centuries;
	}
}
