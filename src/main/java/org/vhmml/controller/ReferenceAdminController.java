package org.vhmml.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.dto.zotero.ZoteroCollectionImportResult;
import org.vhmml.dto.zotero.ZoteroItemImportResult;
import org.vhmml.dto.zotero.ZoteroReferenceGroup;
import org.vhmml.dto.zotero.ZoteroSearchResult;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ApplicationConfigService.Property;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.ReferenceService;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.syndication.io.FeedException;

@Controller
@RequestMapping("/reference/admin")
public class ReferenceAdminController {
	
	private static final Logger LOG = Logger.getLogger(ReferenceAdminController.class);
	
	public static final String VIEW_REFERENCE_IMPORT = "reference/import";
	
	public static final String ATT_REFERENCE_GROUPS = "referenceGroups";
	
	static final ObjectMapper objectMapper = new ObjectMapper();	
			
	@Autowired
	private ApplicationConfigService configService;
	
	@Autowired
	private ReferenceService referenceService;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@Autowired
	private PagingUtil pagingUtil;
	
	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@RequestMapping("/import")
	public ModelAndView viewImport() throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_REFERENCE_IMPORT);
		Integer zoteroUserId = configService.getIntValue(Property.ZOTERO_USER_ID);
		String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"title"});		
		List<ZoteroReferenceGroup> groups = referenceService.getReferenceGroupsForUser(zoteroUserId, zoteroAuthKey);
		modelAndView.addObject(ATT_REFERENCE_GROUPS, groups);		
		modelAndView.addObject(ControllerConstants.ATT_SORT_BY, pagingUtil.getSortString(defaultPageable));
		modelAndView.addObject(ControllerConstants.ATT_PAGE_SIZE, defaultPageable.getPageSize());
		modelAndView.addObject(ControllerConstants.ATT_CURRENT_PAGE, defaultPageable.getPageNumber());
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/importCollection")
	public ResponseEntity<ZoteroCollectionImportResult> importCollection(@RequestParam Integer groupId, @RequestParam String collectionKey) throws HttpException, IOException, FeedException {
		String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);
		ZoteroCollectionImportResult importResult = referenceService.importZoteroCollectionEntries(groupId, collectionKey, zoteroAuthKey);
		HttpStatus status = importResult.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		
		return new ResponseEntity<ZoteroCollectionImportResult>(importResult, status); 
	}
	
	@ResponseBody
	@RequestMapping(value = "/importItem", method = RequestMethod.POST)
	public ResponseEntity<ZoteroItemImportResult> importItem(@RequestParam Integer groupId, @RequestParam String itemKey) throws HttpException, IOException, FeedException {
		String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);		
		ZoteroItemImportResult result = referenceService.importZoteroItem(groupId, itemKey, zoteroAuthKey);
		HttpStatus status = result.isSuccess() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
		
		return new ResponseEntity<ZoteroItemImportResult>(result, status); 
	}
	
	@ResponseBody
	@RequestMapping("/getCollectionItems")
	public ZoteroSearchResult getCollectionItems(@RequestParam Integer groupId, @RequestParam String collectionKey, Pageable pageable) throws HttpException, IOException, FeedException {
		String zoteroAuthKey = configService.getValue(Property.ZOTERO_AUTH_KEY);
		return referenceService.getCollectionItems(groupId, collectionKey, zoteroAuthKey, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteEntry(@PathVariable("id") Long id) throws IOException {
		ResponseEntity<String> response = new ResponseEntity<String>("Citation removed successfully.", HttpStatus.OK);
		
		try {
			referenceService.deleteEntry(id);
		} catch(Exception e) {
			LOG.error("Exception attempting to delete reference entry for id " + id, e);
			response = new ResponseEntity<String>("An unexpected error occured while attempting to remove the reference citation. Please check the server logs for more details", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
				
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete/group/{groupId}/collection/{collectionKey}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteByZoteroCollection(@PathVariable("groupId") Integer groupId, @PathVariable("collectionKey") String collectionKey) throws IOException {
		ResponseEntity<String> response = new ResponseEntity<String>("Citations removed successfully.", HttpStatus.OK);
		
		try {
			referenceService.deleteByZoteroCollection(groupId, collectionKey);
			elasticSearchService.reindex(IndexedType.REFERENCE_ITEM, referenceService.findAll());
		} catch(Exception e) {
			LOG.error("Exception attempting to delete reference entries for Zotero group id " + groupId + " and Zotero collection key " + collectionKey, e);
			String errorMessage = "An unexpected error occured while attempting to remove the reference citations. Please check the server logs for more details.";
			response = new ResponseEntity<String>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}		
				
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/reindex", method = RequestMethod.POST)
	public ResponseEntity<String> reindex() {
		ResponseEntity<String> response = new ResponseEntity<String>("Re-index completed successfully.", HttpStatus.OK);
		
		try {
			elasticSearchService.reindex(IndexedType.REFERENCE_ITEM, referenceService.findAll());
		} catch(Exception e) {
			LOG.error("Unexpected exception while trying to re-index reference data.", e);
			response = new ResponseEntity<String>("Re-index failed to complete. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}			
		
		return response;
	}
}
