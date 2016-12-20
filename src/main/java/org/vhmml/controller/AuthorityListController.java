package org.vhmml.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
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
import org.vhmml.entity.readingroom.UniformTitle;
import org.vhmml.exception.ExistingReferencesException;
import org.vhmml.form.AuthorityListObjectForm;
import org.vhmml.service.AuthorityListService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Controller
@RequestMapping("/catalogDatabase/authorityList")
public class AuthorityListController {
	
	private static final Logger LOG = Logger.getLogger(AuthorityListController.class);

	public static final String VIEW_LIST = "catalogDatabase/authorityList";

	public static final String REQUEST_ATT_AUTH_LIST_TYPE = "authListType";
	public static final String REQUEST_ATT_SEARCH_RESULT = "searchResult";
	
	@Autowired
	private AuthorityListService authorityListService;
	
	@Autowired
	private PagingUtil pagingUtil;
	
	private static final ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	@RequestMapping(value = "/{authListType}", method = RequestMethod.GET)
	public ModelAndView viewList(@PathVariable AuthorityListService.AuthorityListType authListType) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_LIST);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"name"});
		AuthorityListSearchResult searchResult = authListSearch(authListType, null, defaultPageable);
		modelAndView.addObject(REQUEST_ATT_SEARCH_RESULT, objectMapper.writeValueAsString(searchResult));
		modelAndView.addObject(REQUEST_ATT_AUTH_LIST_TYPE, authListType);
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/titles", method = RequestMethod.GET)
	public ModelAndView viewTitles() throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_LIST);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"title"});
		TitleSearchResult searchResult = authorityListService.searchContentTitles(null, defaultPageable);
		modelAndView.addObject(REQUEST_ATT_SEARCH_RESULT, objectMapper.writeValueAsString(searchResult));
		modelAndView.addObject(REQUEST_ATT_AUTH_LIST_TYPE, AuthorityListService.AuthorityListType.TITLE);
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public AuthorityListSearchResult authListSearch(AuthorityListService.AuthorityListType authListType, String searchString, Pageable pageable) throws IOException {
		return authorityListService.searchAuthorityList(authListType, searchString, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/search/title", method = RequestMethod.GET)
	public TitleSearchResult searchTitles(String searchString, Pageable pageable) throws IOException {
		return authorityListService.searchContentTitles(searchString, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/save/title", method = RequestMethod.POST)
	public ResponseEntity<Object> saveTitle(Long contentId, String title) throws IOException {
		ResponseEntity<Object> response = null;
		
		try {
			ContentTitleView contentTitleView = authorityListService.saveContentTitle(contentId, title);
			Map<String, Object> responseProps = new HashMap<>();
			responseProps.put("message", "Title " + (StringUtils.isEmpty(title) ? "deleted" : "saved") + " successfully.");
			responseProps.put("object", contentTitleView);
			response = new ResponseEntity<Object>(responseProps, HttpStatus.OK);			
		} catch(Exception e) {
			response = new ResponseEntity<Object>("An unexpected error occurred while trying to save the title, if the problem persists please contact the system administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response; 
	}
	
	@ResponseBody
	@RequestMapping(value = "/contributors/{startsWith}", method = RequestMethod.GET)
	public List<Contributor> searchContributors(@PathVariable String startsWith, Pageable pageable, HttpServletResponse response) {
		return authorityListService.searchContributors(startsWith, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit/{authListType}/{objectId}", method = RequestMethod.GET)
	public ResponseEntity<Object> edit(@PathVariable AuthorityListService.AuthorityListType authListType, @PathVariable Long objectId) {		
		ResponseEntity<Object> response = null;		

		try {	
			NamedIdentifiable object = authorityListService.find(authListType, objectId);
			response = new ResponseEntity<Object>(object, HttpStatus.OK);						
		} catch(Exception e) {
			LOG.error("Unexpected error attempting to find reading room object for id " + objectId, e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying to retrieve " + authListType.getDisplayName().toLowerCase() + " with id " + objectId + ". Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Object> save(@ModelAttribute AuthorityListObjectForm authListObjectForm) {		
		ResponseEntity<Object> response = null;

		try {	
			NamedIdentifiable object = getNamedIdentifiable(authListObjectForm);			
			object = authorityListService.save(object, authListObjectForm.getAuthListType());
			Map<String, Object> responseProps = new HashMap<>();
			responseProps.put("message", authListObjectForm.getAuthListType().getDisplayName() + " saved successfully.");
			responseProps.put("object", object);
			response = new ResponseEntity<Object>(responseProps, HttpStatus.OK);			
		} catch(Exception e) {
			response = new ResponseEntity<Object>("An unexpected error occurred while trying to save the " + authListObjectForm.getAuthListType().getDisplayName().toLowerCase() + ", if the problem persists please contact the system administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
		}	

		return response;
	}
	
	@RequestMapping(value = "/references/{authListType}/{objectId}", method = RequestMethod.GET)
	public ResponseEntity<Object> references(@PathVariable AuthorityListService.AuthorityListType authListType, @PathVariable Long objectId) {		
		ResponseEntity<Object> response = null;		
		List<ReadingRoomObject> references = authorityListService.getReferences(authListType, objectId);
		String message = "The selected " + authListType.getDisplayName().toLowerCase();
			
		if(CollectionUtils.isNotEmpty(references)) {
			message += " is referenced by the following " + references.size() + " objects in Reading Room. <br/><br/>";
			for(ReadingRoomObject object : references) {
				message += object.getHmmlProjectNumber() + "<br/>";
			}
		} else {
			message += " is not referenced by any objects in Reading Room.";
		}		
			
		response = new ResponseEntity<Object>(message, HttpStatus.OK);		

		return response;
	}
	
	@RequestMapping(value = "/delete/{authListType}/{objectId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable AuthorityListService.AuthorityListType authListType, @PathVariable Long objectId) {		
		ResponseEntity<Object> response = null;		

		try {	
			authorityListService.delete(authListType, objectId);
			response = new ResponseEntity<Object>("The " + authListType.getDisplayName().toLowerCase() + " was deleted successfully", HttpStatus.OK);						
		} catch(ExistingReferencesException e) {
			List<ReadingRoomObject> references = e.getReferences();
			String message = "The selected " + authListType.getDisplayName().toLowerCase() + " is referenced by the following " + references.size() + " objects in Reading Room. Please remove these references before deleting the object.<br/><br/>";
			
			for(ReadingRoomObject object : references) {
				message += object.getHmmlProjectNumber() + "<br/>";
			}
			
			response = new ResponseEntity<Object>(message, HttpStatus.PRECONDITION_FAILED);
		} catch(Exception e) {
			LOG.error("Unexpected error attempting to unlock reading room object for id " + objectId, e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying to retrieve a reference count for the " + authListType.getDisplayName().toLowerCase() + " with id " + objectId + ". Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@SuppressWarnings("incomplete-switch")
	private NamedIdentifiable getNamedIdentifiable(AuthorityListObjectForm form) {
		NamedIdentifiable entity = null;
		
		switch(form.getAuthListType()) {
			case COUNTRY:
				entity = new Country(form.getId(), form.getName(), form.getAuthorityUriLC(), form.getAuthorityUriVIAF());
				break;
			case CITY:
				City dbCity = authorityListService.find(form.getAuthListType(), form.getId());
				entity = new City(form.getId(), form.getName(), form.getAuthorityUriLC(), form.getAuthorityUriVIAF(), dbCity.getCountryId());
				break;
			case REPOSITORY:
				Repository dbRepo = authorityListService.find(form.getAuthListType(), form.getId());				
				entity = new Repository(form.getId(), form.getName(), form.getAuthorityUriLC(), form.getAuthorityUriVIAF(), dbRepo.getCityId());
				break;
			case FEATURE:
				entity = new Feature(form.getId(), form.getName());
				break;
			case GENRE:
				entity = new Genre(form.getId(), form.getName(), form.getUri());
				break;
			case CONTAINER:
				entity = new ArchivalContainer(form.getId(), form.getName());
				break;
			case FORMAT:
				entity = new Format(form.getId(), form.getName());
				break;
			case LANGUAGE:
				entity = new Language(form.getId(), form.getName());
				break;
			case INSTITUTION:
				entity = new Institution(form.getId(), form.getName());
				break;
			case UNIFORM_TITLE:
				entity = new UniformTitle(form.getId(), form.getName(), form.getAuthorityUriLC(), form.getAuthorityUriVIAF());
				break;
			case CONTRIBUTOR:
				entity = new Contributor(form.getId(), form.getName(), form.getAuthorityUriLC(), form.getAuthorityUriVIAF(), form.getDisplayName());
				break;
		}
		
		return entity;
	}
}
