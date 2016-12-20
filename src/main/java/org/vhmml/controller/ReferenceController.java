package org.vhmml.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.ReferenceEntryView;
import org.vhmml.dto.ReferenceSearchResult;
import org.vhmml.dto.zotero.ZoteroReferenceGroup;
import org.vhmml.entity.ReferenceEntry;
import org.vhmml.entity.ReferenceListItem;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ApplicationConfigService.Property;
import org.vhmml.service.ReferenceService;
import org.vhmml.service.ReferenceService.SearchType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/reference")
public class ReferenceController {
	
	private static final Logger LOG = Logger.getLogger(ReferenceController.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper();		
	
	public static final String VIEW_REFERENCE_HOME = "reference/home";
	public static final String VIEW_REFERENCE_HELP = "reference/help";
	
	public static final String ATT_REFERENCE_ENTRIES = "referenceEntries";
	public static final String ATT_INITIAL_SEARCH_RESULT = "initialSearchResult";
	public static final String ATT_SELECTED_ENTRY = "selectedEntry";
	public static final String ATT_SEARCH_TYPE = "searchType";
	public static final String ATT_SEARCH_STRING = "searchString";
	public static final String ATT_REFERENCE_GROUPS = "referenceGroups";
	
	@Autowired
	private ApplicationConfigService configService;
		
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Autowired
	private ReferenceService referenceService;
	
	@Autowired
	private PagingUtil pagingUtil;
	
	@RequestMapping({"", "/"})
	public ModelAndView home(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_REFERENCE_HOME);
		
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"author", "title", "date"});		
		ReferenceSearchResult searchResult = search(null, null, defaultPageable);
		
		if(request.isUserInRole(Role.Name.ROLE_REFERENCE_CREATOR.name()) || request.isUserInRole(Role.Name.ROLE_ADMIN.name())) {
			Integer zoteroUserId = configService.getIntValue(Property.ZOTERO_USER_ID);
			String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);
			List<ZoteroReferenceGroup> groups = referenceService.getReferenceGroupsForUser(zoteroUserId, zoteroAuthKey);
			modelAndView.addObject(ATT_REFERENCE_GROUPS, groups);
		}			
		
		// default to keyword search
		modelAndView.addObject(ATT_SEARCH_TYPE, SearchType.KEYWORD);
		modelAndView.addObject(ATT_INITIAL_SEARCH_RESULT, objectMapper.writeValueAsString(searchResult));		
		modelAndView.addObject(ControllerConstants.ATT_SORT_BY, pagingUtil.getSortString(defaultPageable));
		modelAndView.addObject(ControllerConstants.ATT_PAGE_SIZE, defaultPageable.getPageSize());
		modelAndView.addObject(ControllerConstants.ATT_CURRENT_PAGE, defaultPageable.getPageNumber());
		
		return modelAndView;
	}
	
	@RequestMapping("/entry/{entryId}")
	public ModelAndView viewEntry(@PathVariable Long entryId, HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_REFERENCE_HOME);		
		ReferenceSearchResult searchResult = new ReferenceSearchResult();
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"author", "title", "date"});			
		ReferenceEntryView selectedEntry = new ReferenceEntryView(referenceService.getEntry(entryId));			
		modelAndView.addObject(ATT_SELECTED_ENTRY, objectMapper.writeValueAsString(selectedEntry));
		ReferenceListItem listItem = referenceService.findListItemById(entryId);
		searchResult.setItems(Arrays.asList(listItem));		
		searchResult.setPageNumber(0);
		searchResult.setPageSize(defaultPageable.getPageSize());
		searchResult.setTotalElements(1L);
		
		if(request.isUserInRole(Role.Name.ROLE_REFERENCE_CREATOR.name()) || request.isUserInRole(Role.Name.ROLE_ADMIN.name())) {
			Integer zoteroUserId = configService.getIntValue(Property.ZOTERO_USER_ID);
			String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);
			List<ZoteroReferenceGroup> groups = referenceService.getReferenceGroupsForUser(zoteroUserId, zoteroAuthKey);
			modelAndView.addObject(ATT_REFERENCE_GROUPS, groups);
		}			
		
		modelAndView.addObject(ATT_INITIAL_SEARCH_RESULT, objectMapper.writeValueAsString(searchResult));
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {		
		return new ModelAndView(VIEW_REFERENCE_HELP);
	}
	
	@ResponseBody
	@RequestMapping("/search")
	public ReferenceSearchResult search(@RequestParam(required = false) SearchType searchType, @RequestParam(required = false) String searchString, Pageable pageable) {
		Long start = new Date().getTime();
		ReferenceSearchResult result = referenceService.search(searchType, searchString, pageable);
		
		/* TODO: this is an example of how to do a full text index search if we upgrade MySQL or use MariaDB and decide NOT
		 * to use a search engine like Elastic Search. If we go this route we still need to find a way to make
		 * sorting work correctly since the only way to call a full text query is with a native query and those don't 
		 * support Spring paging.
		 */
//		referenceService.searchNative(searchString, pageable);
		LOG.info("search took " + (new Date().getTime() - start));
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/getEntry")
	public ReferenceEntryView getEntry(@RequestParam Long entryId) {
		ReferenceEntry entry = referenceService.getEntry(entryId);
		return new ReferenceEntryView(entry);
	}
	
	@RequestMapping(value = "/emailEntryToUser", method = RequestMethod.POST)
	public ResponseEntity<String> emailEntryToUser(@RequestParam Long entryId, @RequestParam(required = false) String emailAddress) {
		ResponseEntity<String> response = new ResponseEntity<String>("message sent!", HttpStatus.OK);
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(auth != null && !auth.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {
				emailAddress = ((User)auth.getPrincipal()).getUsername();
			}
			
			referenceService.sendEntryToUser(entryId, emailAddress);
		} catch(Exception e) {			
			LOG.error("Unexpected exception attempting to email reference entry " + entryId + " to email address: " + emailAddress, e);
			response = new ResponseEntity<String>("Unexpected error, message not sent.", HttpStatus.INTERNAL_SERVER_ERROR);			
		}		
		
		return response;
	}
}
