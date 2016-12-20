package org.vhmml.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.ReadingRoomSearchResult;
import org.vhmml.dto.VhmmlSearchParameters;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.service.AuthorityListService;
import org.vhmml.service.ReadingRoomService;
import org.vhmml.web.VhmmlSession;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/readingRoom")
public class ReadingRoomController extends BaseReadingRoomController {
	
	private static final Logger LOG = Logger.getLogger(ReadingRoomController.class);
	
	public static final String VIEW_READING_ROOM_HOME = "readingRoom/home";
	public static final String VIEW_READING_ROOM_HELP = "readingRoom/help";
	public static final String VIEW_READING_ROOM_COLLECTIONS = "readingRoom/collections";
	public static final String VIEW_READING_ROOM_UNCATALOGED_COLLECTIONS = "readingRoom/uncatalogedCollections";
	
	public static final String PARAM_SELECTED_OBJECT_JSON = "selectedObjectJson";	
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private AuthorityListService authorityListService;
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);		
	}
	
	@RequestMapping(value = { "", "/" })
	public ModelAndView home(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_READING_ROOM_HOME);
		addReadingRoomLists(modelAndView.getModel(), request);
		VhmmlSession session = VhmmlSession.getSession(request);
		
		if(session.getSavedReadingRoomSearch() != null) {
			request.setAttribute(ATT_SAVED_SEARCH_JSON, objectMapper.writeValueAsString(session.getSavedReadingRoomSearch()));
		}
		
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = {"/search", "/search/"}, method = RequestMethod.GET)
	public ResponseEntity<Object> search(@ModelAttribute VhmmlSearchParameters searchParameters, Pageable pagingData, HttpServletRequest request) {
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to search Reading Room. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		VhmmlSession.getSession(request).setSavedReadingRoomSearch(null);
		
		try {
			ReadingRoomSearchResult result = readingRoomService.search(searchParameters.getSearchTerms(), pagingData);
			response = new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch(Exception e) {
			LOG.error("Unexpected exception running search", e);
		}
		
		return response;
	}
		
	@RequestMapping(value = {"/view/{objectId}", "/view/{objectId}/"}, method = RequestMethod.GET)
	public ModelAndView viewObject(@PathVariable Long objectId, HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = home(request);
		ReadingRoomObject object = readingRoomService.findById(objectId);
		
		if(object != null) {
			modelAndView.addObject(PARAM_SELECTED_OBJECT_JSON, objectMapper.writeValueAsString(object));
		}
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/object/{objectId}", "/object/{objectId}/"})
	public ReadingRoomObject getObject(@PathVariable Long objectId) {
		return readingRoomService.findById(objectId);
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {
		return new ModelAndView(VIEW_READING_ROOM_HELP);
	}
	
	@RequestMapping("/collections")
	public ModelAndView viewCollections() {
		return new ModelAndView(VIEW_READING_ROOM_COLLECTIONS);
	}
	
	@RequestMapping("/uncatalogedCollections")
	public ModelAndView viewUncatalogedCollections() {
		return new ModelAndView(VIEW_READING_ROOM_UNCATALOGED_COLLECTIONS);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getCities/{countryId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getCities(@PathVariable Long countryId) {
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to retrieve cities for the selected country. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);

		try {
			List<City> cities = authorityListService.getCitiesByCountry(countryId);
			response = new ResponseEntity<Object>(cities, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Unable to retrieve cities for country " + countryId, e);
		}

		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRepositories/{cityId}", method = RequestMethod.GET)
	public ResponseEntity<Object> getRepositories(@PathVariable Long cityId) {
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to retrieve repositories for the selected city. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);

		try {
			List<Repository> repos = authorityListService.getRepositoriesByCity(cityId);
			response = new ResponseEntity<Object>(repos, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Unable to retrieve repositories for city " + cityId, e);
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/saveSearchState")
	public void saveSearchState(@ModelAttribute VhmmlSearchParameters searchParameters, Pageable pagingData, @RequestParam(required = false) String postLoginDestination, HttpServletRequest request) {
		VhmmlSession session = VhmmlSession.getSession(request);
		
		if(StringUtils.isNotEmpty(postLoginDestination)) {
			session.setPostLoginDestination(postLoginDestination);
		}
		
		if(searchParameters.getSearchTerms() != null) {
			// we have to set the paging data rather than send it up as part of the @ModelAttribute because Spring has to instantiate the Pageable
			// due to the fact that it's an interface and the PageRequest implementation doesn't have a default constructor
			searchParameters.setPagingData(pagingData);
			session.setSavedReadingRoomSearch(searchParameters);
		}				
	}
}
