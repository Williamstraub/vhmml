package org.vhmml.controller;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.ReadingRoomService;


@Controller
@RequestMapping("/readingRoom/admin")
public class ReadingRoomAdminController {
	
	private static final Logger LOG = Logger.getLogger(ReadingRoomAdminController.class);
	
	public static final String PARAM_READING_ROOM_OBJECT = "readingRoomObject";
	public static final String PARAM_REPOSITORIES = "repositories";
	public static final String PARAM_OBJECT_TYPES = "objectTypes";
	public static final String PARAM_FORMATS = "formats";
	public static final String PARAM_COUNTRIES = "countries";
	public static final String PARAM_LANGUAGES = "languages";
	public static final String PARAM_SUPPORTS = "supports";
	public static final String PARAM_GENRES = "genres";
	public static final String PARAM_SUBJECTS = "subjects";
	
	@Autowired
	private ReadingRoomService readingRoomService;	
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	public ReadingRoomAdminController() {
		super();
	}
	
	@ResponseBody
	@RequestMapping(value = "/reindex", method = RequestMethod.POST)
	public ResponseEntity<String> reindex() {
		ResponseEntity<String> response = new ResponseEntity<String>("Re-index completed successfully.", HttpStatus.OK);
		
		try {
			List<ReadingRoomObjectOverview> readingRoomObjectOverviews = readingRoomService.getAllOverviews();
			elasticSearchService.reindex(IndexedType.READING_ROOM_OBJECT, readingRoomObjectOverviews);
		} catch(Exception e) {
			LOG.error("Unexpected exception while trying to re-index reference data.", e);
			response = new ResponseEntity<String>("Re-index failed to complete. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}			
		
		return response;
	}
}
