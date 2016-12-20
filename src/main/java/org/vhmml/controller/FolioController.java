package org.vhmml.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.FolioSearchResult;
import org.vhmml.dto.VhmmlSearchParameters;
import org.vhmml.entity.FolioObject;
import org.vhmml.service.FolioService;
import org.vhmml.service.ImageService;
import org.vhmml.web.VhmmlSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/folio")
public class FolioController extends BaseValidationController {
	
	private static final Logger LOG = Logger.getLogger(FolioController.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static final String VIEW_FOLIO_HOME = "folio/home";
	public static final String VIEW_FOLIO_HELP = "folio/help";
	public static final String VIEW_FOLIO_EDIT = "folio/edit";
	
	private static final String PARAM_FOLIO_OBJECT = "folioObject";
	private static final String PARAM_IMAGE_SERVER = "imageServer";
	private static final String PARAM_LANGUAGES = "languages";
	private static final String PARAM_WRITING_SYSTEMS = "writingSystems";
	private static final String PARAM_SCRIPTS = "scripts";
	private static final String PARAM_SELECT_OBJECT_JSON = "selectedObjectJson";
	
	private static final String RESPONSE_DELETE_SUCCESS = "DELETE_SUCCESS";
	
	@Autowired
	private FolioService folioService;
	
	@Autowired
	private ImageService imageService;
	
	@RequestMapping({"", "/"})
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView(VIEW_FOLIO_HOME);
		modelAndView.addObject(PARAM_LANGUAGES, folioService.findLanguages());
		modelAndView.addObject(PARAM_WRITING_SYSTEMS, folioService.findWritingSystems());
		modelAndView.addObject(PARAM_SCRIPTS, folioService.findScripts());
		
		return modelAndView;
	}
	
	@RequestMapping({"/view/{id}"})
	public ModelAndView viewObject(@PathVariable Long id) throws IOException {
		ModelAndView modelAndView = home();
		modelAndView.addObject(PARAM_SELECT_OBJECT_JSON, objectMapper.writeValueAsString(folioService.find(id)));
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {
		return new ModelAndView(VIEW_FOLIO_HELP);
	}
	
	@RequestMapping(value = "/admin/add", method = RequestMethod.GET)
	public ModelAndView viewAdd() {
		ModelAndView modelAndView = new ModelAndView(VIEW_FOLIO_EDIT);
		FolioObject folioObject = new FolioObject();
		folioObject.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
		modelAndView.addObject(PARAM_FOLIO_OBJECT, folioObject);
		modelAndView.addObject(PARAM_IMAGE_SERVER, imageService.getImageServerHost());
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/edit/{id}", method = RequestMethod.GET)
	public ModelAndView viewEdit(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView(VIEW_FOLIO_EDIT);
		modelAndView.addObject(PARAM_FOLIO_OBJECT, folioService.find(id));
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/save", method = RequestMethod.POST)
	public ResponseEntity<Object> save(@ModelAttribute FolioObject folioObject, BindingResult result) throws IOException {		
		ResponseEntity<Object> saveResponse = null;		
		boolean formValidationErrors = result.hasErrors();

		try {
			if (!formValidationErrors) {
				FolioObject object = folioService.save(folioObject);
				Map<String, Object> responseParams = new HashMap<>();
				responseParams.put("message", "Changes saved successfully.");
				responseParams.put("objectId", object.getId());
				responseParams.put("projectNumber", object.getFolioObjectNumber());
				saveResponse = new ResponseEntity<Object>(responseParams, HttpStatus.OK);
			} else {			
				saveResponse = new ResponseEntity<Object>(result.getAllErrors(), HttpStatus.BAD_REQUEST);						
			}
		} catch(Exception e) {
			LOG.error("Unexpected error saving reading room object", e);
			saveResponse = new ResponseEntity<Object>("There was an unexpected error while trying to save. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		

		return saveResponse;
	}
	
	@ResponseBody
	@RequestMapping(value = "/admin/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id, HttpServletResponse httpResponse) throws IOException {
		ResponseEntity<String> response = null;

		try {					
			folioService.delete(id);
			response = new ResponseEntity<String>(RESPONSE_DELETE_SUCCESS, HttpStatus.OK);						
		} catch (Exception e) {
			LOG.error("Unexpected error while trying to delete folio object for id " + id, e);
			response = new ResponseEntity<String>("There was an unexpected error while trying to delete the record. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/search", "/search/"}, method = RequestMethod.GET)
	public ResponseEntity<Object> search(@ModelAttribute VhmmlSearchParameters searchParameters, Pageable pagingData, HttpServletRequest request) {
		ResponseEntity<Object> response = new ResponseEntity<Object>("Unable to search Reading Room. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		VhmmlSession.getSession(request).setSavedReadingRoomSearch(null);
		
		try {
			FolioSearchResult result = folioService.search(searchParameters.getSearchTerms(), pagingData);
			response = new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch(Exception e) {
			LOG.error("Unexpected exception running search", e);
		}
		
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = {"/object/{objectId}", "/object/{objectId}/"})
	public FolioObject getObject(@PathVariable Long objectId) {
		return folioService.find(objectId);
	}
	
	@ResponseBody
	@RequestMapping(value = "/folioObject/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getReadingRoomObjectValidationRules() {
		return super.getValidationRules(FolioObject.class);
	}
}
