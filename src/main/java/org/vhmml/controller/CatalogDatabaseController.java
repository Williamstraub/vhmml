package org.vhmml.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.ImportResponse;
import org.vhmml.dto.ReadingRoomContentDTO;
import org.vhmml.dto.ReadingRoomPartDTO;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.readingroom.AlternateSurrogate;
import org.vhmml.entity.readingroom.ArchivalContent;
import org.vhmml.entity.readingroom.ArchivalData;
import org.vhmml.entity.readingroom.ArchivalObject;
import org.vhmml.entity.readingroom.BibliographyUrl;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.Content;
import org.vhmml.entity.readingroom.ContentContributor;
import org.vhmml.entity.readingroom.Contributor;
import org.vhmml.entity.readingroom.Contributor.Type;
import org.vhmml.entity.readingroom.Country;
import org.vhmml.entity.readingroom.Extent;
import org.vhmml.entity.readingroom.FacsimileUrl;
import org.vhmml.entity.readingroom.Feature;
import org.vhmml.entity.readingroom.Format;
import org.vhmml.entity.readingroom.Genre;
import org.vhmml.entity.readingroom.Import;
import org.vhmml.entity.readingroom.Institution;
import org.vhmml.entity.readingroom.Language;
import org.vhmml.entity.readingroom.Manuscript;
import org.vhmml.entity.readingroom.ManuscriptAndPrinted;
import org.vhmml.entity.readingroom.PrintedObject;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.ReadingRoomObject.AccessRestriction;
import org.vhmml.entity.readingroom.ReadingRoomObjectContributor;
import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;
import org.vhmml.entity.readingroom.ReadingRoomObjectPart;
import org.vhmml.entity.readingroom.ReadingRoomPartContributor;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.entity.readingroom.Subject;
import org.vhmml.exception.ObjectLockedException;
import org.vhmml.form.ReadingRoomObjectForm;
import org.vhmml.security.VhmmlSecurityUtil;
import org.vhmml.service.AuthorityListService;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.ImageService;
import org.vhmml.service.OliverService;
import org.vhmml.service.ReadingRoomService;
import org.vhmml.util.AttributeUtil;
import org.vhmml.util.FormBeanUtil;
import org.vhmml.web.VhmmlSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Controller
@RequestMapping("/catalogDatabase")
public class CatalogDatabaseController extends BaseReadingRoomController {

	private static final Logger LOG = Logger.getLogger(ReadingRoomAdminController.class);

	public static final String VIEW_HOME = "catalogDatabase/home";
	public static final String VIEW_IMPORT = "catalogDatabase/import";
	public static final String VIEW_EXPORT = "catalogDatabase/export";
	public static final String VIEW_ADD = "catalogDatabase/add";
	public static final String VIEW_CATALOG_HELP = "catalogDatabase/help";	
	
	public static final String RESPONSE_DELETE_SUCCESS = "DELETE_SUCCESS";	

	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private AuthorityListService authorityListService;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private ElasticSearchService elasticSearchService;

	@Autowired
	private OliverService oliverService;	

	private static final ObjectMapper objectMapper;
	
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_HOME);
		addReadingRoomLists(modelAndView.getModel(), request);
		VhmmlSession session = VhmmlSession.getSession(request);
		
		if(session.getSavedReadingRoomSearch() != null) {
			request.setAttribute(ATT_SAVED_SEARCH_JSON, objectMapper.writeValueAsString(session.getSavedReadingRoomSearch()));
		}
		
		return modelAndView;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String viewAdd(ModelMap model, HttpServletRequest request) throws IOException {
		ReadingRoomObjectForm form = new ReadingRoomObjectForm();
		form.setImageServer(imageService.getImageServerHost());
		form.setInputter(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute(PARAM_READING_ROOM_OBJECT_FORM, form);		
		addReadingRoomLists(model, request);

		return VIEW_ADD;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<Object> saveReadingRoomObject(@ModelAttribute @Valid ReadingRoomObjectForm readingRoomObjectForm, BindingResult result, String successView, HttpServletResponse response) throws IOException {		
		ResponseEntity<Object> saveResponse = null;		
		boolean formValidationErrors = result.hasErrors();

		try {
			if (!formValidationErrors) {
				ReadingRoomObject object = createReadingRoomObject(readingRoomObjectForm);
				// always locked while we're editing, it only gets unlocked when the edit page unloads
				object.setLocked(true);
				object.setLockedBy(VhmmlSecurityUtil.getCurrentUser().getUsername());
				Long id = object.getId();
				
				if(id != null) {
					ReadingRoomObject savedObject = readingRoomService.findById(id);
					
					if(!object.getClass().equals(savedObject.getClass())) {
						readingRoomService.updateObjectType(id, readingRoomObjectForm.getRecordType());
					}
				}
				
				object = readingRoomService.saveReadingRoomObject(object);
				Map<String, Object> responseParams = new HashMap<>();
				responseParams.put("message", "Changes saved successfully.");
				responseParams.put("objectId", object.getId());
				responseParams.put("projectNumber", object.getHmmlProjectNumber());
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
	
	@RequestMapping(value = "/setLocked/{objectId}/{locked}", method = RequestMethod.POST)
	public ResponseEntity<Object> setLocked(@PathVariable Long objectId, @PathVariable Boolean locked) throws IOException {		
		ResponseEntity<Object> response = null;		

		try {
			readingRoomService.setLocked(objectId, locked);			
		} catch(ObjectLockedException e) {
			// already locked by another user
			response = new ResponseEntity<Object>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		} catch(Exception e) {
			LOG.error("Unexpected error attempting to " + (locked ? "lock" : "unlock") + " reading room object for id " + objectId, e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying to unlock the record for editing. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		

		return response;
	}
	
	@RequestMapping(value = "/deletePart/{partId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deletePart(@PathVariable Long partId, HttpServletResponse httpResponse) throws IOException {
				
		ResponseEntity<String> response = null;

		try {
			readingRoomService.deleteReadingRoomObjectPart(partId);
			response = new ResponseEntity<String>(RESPONSE_DELETE_SUCCESS, HttpStatus.OK);			
		} catch(IllegalAccessException e) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOG.error("Unexpected error while trying to delete reading room object part for id " + partId, e);
			response = new ResponseEntity<String>("There was an unexpected error while trying to delete the part. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	@RequestMapping(value = "/deleteContentItem/{contentItemId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteContentItem(@PathVariable Long contentItemId, HttpServletResponse httpResponse) throws IOException {
				
		ResponseEntity<String> response = null;

		try {
			readingRoomService.deleteReadingRoomContent(contentItemId);
			response = new ResponseEntity<String>(RESPONSE_DELETE_SUCCESS, HttpStatus.OK);			
		} catch(IllegalAccessException e) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOG.error("Unexpected error while trying to delete content item for id " + contentItemId, e);
			response = new ResponseEntity<String>("There was an unexpected error while trying to delete the content item. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}	
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editReadingRoomObject(Long id, HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
		ModelAndView modelAndView = new ModelAndView(VIEW_ADD);
		ReadingRoomObject object = readingRoomService.findById(id);
					
		// spring security is set up to only allow level 1 & level 2 catalogers to hit this url,
		// but for level 2 catalogers they need to be the creator of the record so we check that here
		if(!readingRoomService.currentUserCanEdit(object)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} else {
			ReadingRoomObjectForm form = new ReadingRoomObjectForm(object);
			form.setHasParts(object.getParts().size() > 0);
			form.setImageServer(imageService.getImageServerHost());
			modelAndView.addObject(PARAM_READING_ROOM_OBJECT_FORM, form);
			modelAndView.addObject(PARAM_READING_ROOM_OBJECT_FORM_JSON, objectMapper.writeValueAsString(form));
			addReadingRoomLists(modelAndView.getModel(), request);
		}
		
		return modelAndView;
	}
		
	@ResponseBody
	@RequestMapping(value = "/delete/{readingRoomObjectId}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteReadingRoomObject(@PathVariable Long readingRoomObjectId, HttpServletResponse httpResponse) throws IOException {
		ResponseEntity<String> response = null;

		try {					
			readingRoomService.deleteReadingRoomObject(readingRoomObjectId);
			response = new ResponseEntity<String>(RESPONSE_DELETE_SUCCESS, HttpStatus.OK);						
		} catch(IllegalAccessException e) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (Exception e) {
			LOG.error("Unexpected error while trying to delete reading room object for id " + readingRoomObjectId, e);
			response = new ResponseEntity<String>("There was an unexpected error while trying to delete the record. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@RequestMapping(value = "/addListOption", method = RequestMethod.POST)
	public ResponseEntity<Object> addListOption(@RequestParam AuthorityListService.AuthorityListType type, @RequestParam String name, @RequestParam(required = false) String authorityUriLC, @RequestParam(required = false) String authorityUriVIAF, @RequestParam(required = false) Long parentOptionId) {
		ResponseEntity<Object> response = null;

		try {
			Object newOption = authorityListService.addListOption(type, name, authorityUriLC, authorityUriVIAF, parentOptionId);
			response = new ResponseEntity<Object>(newOption, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Unexpected error while adding authority list option of type " + type + " in reading room", e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying to add the " + type.getDisplayName() + ". Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@RequestMapping("/import/delete/{importId}")
	public ResponseEntity<ImportResponse> deleteImport(@PathVariable Long importId) {		
		ImportResponse importResponse = new ImportResponse("Import deleted successfully.");
		ResponseEntity<ImportResponse> response = new ResponseEntity<ImportResponse>(importResponse, HttpStatus.OK);
		
		try {			
			oliverService.deleteImportedData(importId);
		} catch (Exception e) {
			String errorMessage = ExceptionUtils.getRootCauseMessage(e);						
			LOG.error("Unexpected exception while trying to deleted imported data.", e);
			importResponse.setMessage("Delete of imported data failed to complete with the following error: " + errorMessage + ".");			
			response = new ResponseEntity<ImportResponse>(importResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.getBody().setImports(oliverService.getImports());
		
		return response;		
	}

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public ModelAndView viewImport() throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_IMPORT);
		modelAndView.addObject(PARAM_IMPORTS, objectMapper.writeValueAsString(oliverService.getImports()));

		return modelAndView;
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public ModelAndView export() {
		return new ModelAndView(VIEW_EXPORT);
	}
	
	@RequestMapping(value = "/exportObjects", method = RequestMethod.GET)
	public void exportObjects(@RequestParam String collectionName, @RequestParam Long exportToken, HttpServletResponse response) {
		
		try {
			List<ReadingRoomObject> objects = getObjectsForExport(collectionName);			
			String jsonData = objectMapper.writeValueAsString(objects);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String downloadFilename = "Reading Room " + (StringUtils.isNoneEmpty(collectionName) ? collectionName : "All Collections") + " Export " + format.format(new Date()) + ".json";
			InputStream inputStream = IOUtils.toInputStream(jsonData, "UTF-8");
			response.setContentType("application/json");
			response.addCookie(new Cookie("exportToken", exportToken.toString()));
			response.addHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch(Exception e) {
			LOG.error("Unexpected exception while attempting to export Reading Room data", e);
		}		
	}
	
	@RequestMapping(value = "/exportLinks", method = RequestMethod.GET)
	public void exportLinks(@RequestParam String collectionName, @RequestParam Long exportToken, HttpServletResponse response) {
		
		try {			
			List<ReadingRoomObject> objects = getObjectsForExport(collectionName);
			StringBuilder csvData = new StringBuilder();
			csvData.append("HMML Project Number").append(",").append("Permanent Link").append("\n");
			
			for(ReadingRoomObject object : objects) {
				csvData.append(object.getHmmlProjectNumber()).append(",").append("https://w3id.org/vhmml/readingRoom/view/").append(object.getId()).append("\n");
			}
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String downloadFilename = "Reading Room Links " + (StringUtils.isNoneEmpty(collectionName) ? collectionName : "All Collections ") + format.format(new Date()) + ".csv";
			InputStream inputStream = IOUtils.toInputStream(csvData.toString(), "UTF-8");
			response.setContentType("text/csv");
			response.addCookie(new Cookie("exportToken", exportToken.toString()));
			response.addHeader("Content-Disposition", "attachment;filename=" + downloadFilename);
			IOUtils.copy(inputStream, response.getOutputStream());
			response.flushBuffer();
		} catch(Exception e) {
			LOG.error("Unexpected exception while attempting to export Reading Room data", e);
		}		
	}
	
	private List<ReadingRoomObject> getObjectsForExport(String collectionName) {
		List<ReadingRoomObject> objects = null;
		
		if(StringUtils.isEmpty(collectionName)) {
			objects = readingRoomService.getAllObjects();
		} else {
			objects = readingRoomService.findByCollection(collectionName);
		}
		
		return objects;
	}

	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public ResponseEntity<ImportResponse> importData(@RequestParam MultipartFile importFile) {
		ImportResponse importResponse = new ImportResponse("Data import completed successfully.");
		ResponseEntity<ImportResponse> response = new ResponseEntity<ImportResponse>(importResponse, HttpStatus.OK);
		String importFileName = importFile.getOriginalFilename();
		Import importJob = null;
		
		try {
			File tempDir = FileUtils.getTempDirectory();		
			String tempFileName = tempDir.getAbsolutePath() + File.separator + importFileName;
			File tempFile = new File(tempFileName);
			FileUtils.writeByteArrayToFile(tempFile, importFile.getBytes());
			importJob = oliverService.createImportJob(tempFileName);
			oliverService.runImportJob(importJob);	
		} catch (Exception e) {
			String errorMessage = ExceptionUtils.getRootCauseMessage(e);
			importJob.setStatus(Import.Status.FAILED);
			importJob.setErrorMessage(errorMessage);
			oliverService.saveImportJob(importJob);
			LOG.error("Unexpected exception while trying to import data from file " + importFileName, e);
			importResponse.setMessage("Database import failed to complete with the following error: " + errorMessage + ".");			
			response = new ResponseEntity<ImportResponse>(importResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.getBody().setImports(oliverService.getImports());
		
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/reindex", method = RequestMethod.POST)
	public ResponseEntity<String> reindex() {
		ResponseEntity<String> response = new ResponseEntity<String>("Re-index completed successfully.", HttpStatus.OK);

		try {
			List<ReadingRoomObjectOverview> readingRoomObjectOverviews = readingRoomService.getAllOverviews();
			elasticSearchService.reindex(IndexedType.READING_ROOM_OBJECT, readingRoomObjectOverviews);
		} catch (Exception e) {
			LOG.error("Unexpected exception while trying to re-index reference data.", e);
			response = new ResponseEntity<String>("Re-index failed to complete. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/readingRoomObject/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getReadingRoomObjectValidationRules() {
		return super.getValidationRules(ReadingRoomObjectForm.class);
	}	
	
	@RequestMapping("/help")
	public ModelAndView help() {
		return new ModelAndView(VIEW_CATALOG_HELP);
	}

	@SuppressWarnings("unchecked")
	private ReadingRoomObject createReadingRoomObject(ReadingRoomObjectForm form) {
		ReadingRoomObject object = null;

		switch (form.getRecordType()) {
			case MANUSCRIPT:			
				object = new Manuscript();				
				break;
			case MANUSCRIPT_PRINT:
				object = new ManuscriptAndPrinted();
				break;
			case ARCHIVAL_OBJECT:
				object = new ArchivalObject();
				break;
			case READING_ROOM_OBJECT:
				object = new ReadingRoomObject();
				break;
			case PRINT:
				object = new PrintedObject();
		}
		
		BeanUtils.copyProperties(form, object, "parts");
		object.setId(form.getReadingRoomObjectId());		
		object.setSurrogateFormat(new Format(form.getSurrogateFormatId()));
		object.setExternalBibliographyUrls((List<BibliographyUrl>)FormBeanUtil.getPopulatedList(form.getExternalBibliographyUrls()));
		object.setExternalFacsimileUrls((List<FacsimileUrl>)FormBeanUtil.getPopulatedList(form.getExternalFacsimileUrls()));
		List<AlternateSurrogate> altSurrogates = new ArrayList<AlternateSurrogate>();
		List<String> surrogates = (List<String>)FormBeanUtil.getPopulatedList(form.getAlternateSurrogateFormats());
		
		for (String altSurrogate : surrogates) {
			altSurrogates.add(new AlternateSurrogate(altSurrogate, object.getId()));
		}

		object.setAlternateSurrogates(altSurrogates);
		object.setAccessRestriction(AccessRestriction.valueOf(form.getAccessRestriction()));
		object.setCountry(new Country(form.getCountryId()));
		object.setCity(new City(form.getCityId()));
		object.setRepository(new Repository(form.getRepositoryId()));
		
		if(form.getHoldingInstitutionId() != null) {
			object.setHoldingInstitution(new Institution(form.getHoldingInstitutionId()));
		}
		
		object.setExtents((List<Extent>)FormBeanUtil.getPopulatedList(form.getExtents()));
		object.setSubjects((List<Subject>)FormBeanUtil.getPopulatedList(form.getSubjects()));		
		object.setGenres((List<Genre>)FormBeanUtil.getPopulatedList(form.getUniqueGenres()));
		
		if(CollectionUtils.isNotEmpty(form.getFeatureIds())) {
			List<Feature> features = new ArrayList<Feature>();
			
			for(Long featureId : form.getFeatureIds()) {
				features.add(new Feature(featureId));
			}
			
			object.setFeatures(features);
		}

		List<ReadingRoomObjectContributor> contributors = new ArrayList<ReadingRoomObjectContributor>();
		List<Contributor> formerOwners = (List<Contributor>)FormBeanUtil.getPopulatedList(form.getFormerOwners());
		
		for (Contributor contributor : formerOwners) {
			ReadingRoomObjectContributor objectContributor = new ReadingRoomObjectContributor();
			objectContributor.setType(Type.FORMER_OWNER);
			objectContributor.setContributor(contributor);
			objectContributor.setReadingRoomObject(object);
			contributors.add(objectContributor);
		}

		object.setContributors(contributors);
		
		if(form.getCaptureDateYear() != null && form.getCaptureDateMonth() != null && form.getCaptureDateDay() != null) {
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, form.getCaptureDateYear());
			cal.set(Calendar.MONTH, form.getCaptureDateMonth());
			cal.set(Calendar.DAY_OF_MONTH, form.getCaptureDateDay());
			object.setCaptureDate(new Date(cal.getTimeInMillis()));
		}
		
		// archival objects don't have parts, they just have archival data object
		if(ReadingRoomObject.Type.ARCHIVAL_OBJECT == form.getRecordType()) {
			ArchivalObject archivalObject = (ArchivalObject)object;			
			archivalObject.setArchivalData(createArchivalData(form));
		} else {
			object.setParts(createReadingRoomObjectParts(form));
		}

		return object;
	}	
	
	@SuppressWarnings("unchecked")
	private List<ReadingRoomObjectPart> createReadingRoomObjectParts(ReadingRoomObjectForm form) {
		List<ReadingRoomObjectPart> parts = new ArrayList<ReadingRoomObjectPart>();
		
		if(form.getHasParts()) {
			for(ReadingRoomPartDTO partDto : form.getParts()) {
				ReadingRoomObjectPart newPart = new ReadingRoomObjectPart();
				BeanUtils.copyProperties(partDto, newPart, "contents");
				
				// can't save just the day
				if(newPart.getDatePreciseYear() == null && newPart.getDatePreciseMonth() == null) {
					newPart.setDatePreciseDay(null);
				}
				newPart.setSupport(AttributeUtil.getListAttributeAsString(partDto.getSupports()));
				newPart.setWritingSystem(AttributeUtil.getListAttributeAsString(partDto.getWritingSystems()));
				newPart.setScript(AttributeUtil.getListAttributeAsString(partDto.getScripts()));
				newPart.setFont(AttributeUtil.getListAttributeAsString(partDto.getFonts()));
				
				if(partDto.isHasContent()) {
					newPart.setContents(createReadingRoomObjectContents(partDto.getContents()));
				}
								
				newPart.addPartContributors((List<ReadingRoomPartContributor>)FormBeanUtil.getPopulatedList(partDto.getScribes()), Type.SCRIBE);
				newPart.addPartContributors((List<ReadingRoomPartContributor>)FormBeanUtil.getPopulatedList(partDto.getArtists()), Type.ARTIST);
				newPart.addPartContributors((List<ReadingRoomPartContributor>)FormBeanUtil.getPopulatedList(partDto.getAssociatedNames()));
				
				parts.add(newPart);
			}
		}		
		
		return parts;
	}
	
	@SuppressWarnings("unchecked")
	private List<Content> createReadingRoomObjectContents(List<ReadingRoomContentDTO> contentDtoList) {
		List<Content> contents = new ArrayList<Content>();
		
		if(CollectionUtils.isNotEmpty(contentDtoList)) {
			for(ReadingRoomContentDTO content : contentDtoList) {
				
				Content newContent = new Content(content);
				newContent.setLanguages(getLanguages(content.getLanguageIds()));				
				newContent.addContentContributors((List<ContentContributor>)FormBeanUtil.getPopulatedList(content.getAuthors()), Type.AUTHOR);				
				newContent.addContentContributors((List<ContentContributor>)FormBeanUtil.getPopulatedList(content.getAssociatedNames()));				
				contents.add(newContent);
			}
		}
		
		return contents;
	}
	
	@SuppressWarnings("unchecked")
	private ArchivalData createArchivalData(ReadingRoomObjectForm form) {
		ArchivalData archivalData = form.getArchivalData();
		archivalData.setSupport(AttributeUtil.getListAttributeAsString(archivalData.getSupportList()));
		archivalData.setLanguages(getLanguages(archivalData.getLanguageIds()));
		archivalData.setWritingSystem(AttributeUtil.getListAttributeAsString(archivalData.getWritingSystemList()));
		
		List<ArchivalContent> contents = archivalData.getContent();
		
		// we have the "has content" attribute because Spring sends up lists of empty objects
		if(archivalData.getHasArchivalContent() && CollectionUtils.isNotEmpty(contents)) {
			for(ArchivalContent content : contents) {				
				content.setSupport(AttributeUtil.getListAttributeAsString(content.getSupportList()));
				content.setScript(AttributeUtil.getListAttributeAsString(content.getScriptList()));
				content.setLanguages(getLanguages(content.getLanguageIds()));
				content.setWritingSystem(AttributeUtil.getListAttributeAsString(content.getWritingSystemList()));
				content.addContentContributors((List<ContentContributor>)FormBeanUtil.getPopulatedList(content.getScribes()), Type.SCRIBE);				
				content.addContentContributors((List<ContentContributor>)FormBeanUtil.getPopulatedList(content.getArtists()), Type.ARTIST);
				content.addContentContributors((List<ContentContributor>)FormBeanUtil.getPopulatedList(content.getAssociatedNames()));
			}
		} else {
			archivalData.setContent(null);
		}		
		
		return archivalData;
	}
	
	private List<Language> getLanguages(List<Long> languageIds) {
		List<Language> languages = new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(languageIds)) {			
			for(Long langId : languageIds) {
				languages.add(new Language(langId));
			}
		}
		
		return languages;
	}
	
	@InitBinder
	public void initBinderAll(WebDataBinder binder) {

		// features is a list of feature ids that need to be converted from
		// string to long
		binder.registerCustomEditor(List.class, "features", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
				Long id = null;

				if (element instanceof String) {
					String stringVal = (String) element;
					if (StringUtils.isNotEmpty(stringVal)) {
						id = Long.valueOf(stringVal);
					}
				}

				return id != null ? new Feature(id) : null;
			}
		});
	}
}
