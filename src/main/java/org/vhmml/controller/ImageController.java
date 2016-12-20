package org.vhmml.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.vhmml.entity.FolioObject;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.service.FolioService;
import org.vhmml.service.ImageService;
import org.vhmml.service.ReadingRoomService;
import org.vhmml.web.VhmmlSession;

@Controller
@RequestMapping("/image")
public class ImageController {

	private static final Logger LOG = Logger.getLogger(ImageController.class);
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private FolioService folioService;
	
	@RequestMapping(value = "/loadData", method = RequestMethod.GET)
	public void loadExternalData(@RequestParam String url, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, URISyntaxException {
		// Mirador doesn't know where it's making requests so it will also proxy requests to vhmml through this method, when that happens we redirect to the requested url
		URL urlObject = new URL(url);
		
		if(isExternalRequest(urlObject, request)) {
			LOG.info("Retrieving external data from URL " + url);
			String jsonData = imageService.getExternalIIIFData(url);
			LOG.info(jsonData);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(jsonData);
		} else {
			String requestPath = urlObject.getPath().substring(request.getContextPath().length());			
			request.getRequestDispatcher(requestPath).forward(request, response);
		}					
	}
	
	@RequestMapping(value = "/manifest/{objectId}", method = RequestMethod.GET)
	public void getProjectManifest(@PathVariable Long objectId, @RequestParam(required = false) String objectType, HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		if(checkImageAccess(objectType, objectId)) {			
			if("folio".equalsIgnoreCase(objectType) || 
				!readingRoomService.mustAcceptUsageAgreement(objectId) || 
				VhmmlSession.getSession(request).isAcceptedReadingRoomAgreement()) {
				
				String manifestJson = imageService.getIIIFManifest(objectType, objectId);
				LOG.debug(manifestJson);
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(manifestJson);
			} else {
				response.getWriter().write("You must accept the Reading Room usage agreement to access the requested manifest");
			}			
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write("You do not have permission to access the requested manifest");
		}
	}

	@RequestMapping(value = "/{objectType}/{projectPath}/{imageName}/info.json", method = RequestMethod.GET)
	public void getImageInfo(@PathVariable String objectType, @PathVariable String projectPath, @PathVariable String imageName, HttpServletResponse response) throws IOException {
				
		if(checkImageAccess(objectType, projectPath)) {
			response.getWriter().write(imageService.getImageInfo(objectType, projectPath, imageName));
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}		
	}

	@RequestMapping(value = "/{objectType}/{collectionName}/{projectNumber}/{imageName}/info.json", method = RequestMethod.GET)
	public void getImageInfo(@PathVariable String objectType, @PathVariable String collectionName, @PathVariable String projectNumber, @PathVariable String imageName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getImageInfo(objectType, collectionName + "/" + projectNumber, imageName, response);	
	}

	@RequestMapping(value = "/imagesPath/{projectNumber}", method = RequestMethod.GET)
	public void getImagesPath(@PathVariable String projectNumber, @RequestParam(required = false) String objectType, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			if(checkImageAccess(objectType, projectNumber)) {
				String imagesPath = imageService.getImagesPath(objectType, projectNumber);
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(imagesPath);
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}				
		} catch(Exception e) {
			LOG.error("Error attempting to get images path for project number " + projectNumber, e);
		}		
	}

	@RequestMapping(value = "/imageList/{projectNumber}", method = RequestMethod.GET)
	public void getImageList(@PathVariable String projectNumber, @RequestParam(required = false) String objectType, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			
			if(checkImageAccess(objectType, projectNumber)) {
				String imageListJson = imageService.getImageList(objectType, projectNumber);
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(imageListJson);
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}			
		} catch(Exception e) {
			LOG.error("Error attempting to get image list for project number " + projectNumber, e);
		}		
	}

	@RequestMapping(value = "/thumbnail/{objectType}/{projectNumber}/{imageName:.+}", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable String objectType, @PathVariable String projectNumber, @PathVariable String imageName, HttpServletResponse response) throws IOException {
		getThumbnail(objectType, projectNumber, imageName, 200, response);
	}

	@RequestMapping(value = "/thumbnail/{objectType}/{projectNumber}/{imageName}/{width}", method = RequestMethod.GET)
	public void getThumbnail(@PathVariable String objectType, @PathVariable String projectNumber, @PathVariable String imageName, @PathVariable Integer width, HttpServletResponse response) throws IOException {
		byte[] imageBytes = imageService.getThumbnail(objectType, projectNumber, imageName, width);
		writeImageToResponse(imageBytes, response);
	}

	@RequestMapping(value = "/{objectType}/{collectionName}/{projectNumber}/{imageName}/{region}/{size}/{rotation}/{qualityAndFormat}", method = RequestMethod.GET)
	public void getImage(@PathVariable String objectType,
			@PathVariable String projectNumber, 
			@PathVariable String collectionName, 
			@PathVariable String imageName, 
			@PathVariable String region, 
			@PathVariable String size, 
			@PathVariable String rotation, 
			@PathVariable String qualityAndFormat,  
			HttpServletResponse response) throws IOException {
	
		getImage(objectType, projectNumber, imageName, region, size, rotation, qualityAndFormat, response);	
	}

	@RequestMapping(value = "/{objectType}/{projectNumber}/{imageName}/{region}/{size}/{rotation}/{qualityAndFormat}", method = RequestMethod.GET)
	public void getImage(@PathVariable String objectType,
			@PathVariable String projectNumber, 
			@PathVariable String imageName, 
			@PathVariable String region, 
			@PathVariable String size, 
			@PathVariable String rotation, 
			@PathVariable String qualityAndFormat,
			HttpServletResponse response) throws IOException {
						
		if(checkImageAccess(objectType, projectNumber)) {
			byte[] imageBytes = imageService.getImage(objectType, projectNumber, imageName, region, size, rotation, qualityAndFormat);
			writeImageToResponse(imageBytes, response);				
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}			
	}
	
	@RequestMapping(value = "/setObjectIcon", method = RequestMethod.POST)
	public ResponseEntity<Object> setObjectIcon(@RequestParam(required = false) String objectType, @RequestParam Long objectId, @RequestParam String iconName) {
		
		ResponseEntity<Object> response = null;
		
		try {
			Object updatedObject = updateObjectIcon(objectType, objectId, iconName);			
			response = new ResponseEntity<Object>(updatedObject, HttpStatus.OK);						
		} catch (Exception e) {
			LOG.error("Unexpected error while setting image icon on object " + objectId, e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying set the image icon. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}	

	@RequestMapping(value = "/clearObjectIcon", method = RequestMethod.POST)
	public ResponseEntity<Object> clearObjectIcon(@RequestParam(required = false) String objectType, @RequestParam Long objectId) {
		
		ResponseEntity<Object> response = null;
		
		try {
			Object updatedObject = updateObjectIcon(objectType, objectId, null);			
			response = new ResponseEntity<Object>(updatedObject, HttpStatus.OK);						
		} catch (Exception e) {
			LOG.error("Unexpected error while setting image icon on object " + objectId, e);
			response = new ResponseEntity<Object>("There was an unexpected error while trying clear the image icon. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}
	
	private Object updateObjectIcon(String objectType, Long objectId, String iconName) {
		Object updatedObject = null;
		
		try {
			if("FOLIO".equalsIgnoreCase(objectType)) {
				FolioObject object = folioService.find(objectId);
				object.setIconName(iconName);
				updatedObject = folioService.save(object);
			} else {
				ReadingRoomObject object = readingRoomService.findById(objectId);
				object.setIconName(iconName);
				updatedObject = readingRoomService.saveReadingRoomObject(object);				
			}			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return updatedObject;
	}
	
	private void writeImageToResponse(byte[] imageBytes, HttpServletResponse response) throws IOException {
		
		if(imageBytes != null) {
			BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			output.write(imageBytes);					
			output.flush();		
		}	
	}
	
	private boolean checkImageAccess(String objectType, Long objectId) {
		// everyone can view folio images
		boolean hasAccess = "folio".equalsIgnoreCase(objectType);
		
		if(!hasAccess) {			
			hasAccess = readingRoomService.checkImageAccess(objectId);
		}
		
		return hasAccess;
	}

	private boolean checkImageAccess(String objectType, String projectNumber) {
		// everyone can view folio images
		boolean hasAccess = "folio".equalsIgnoreCase(objectType);
		
		if(!hasAccess) {
			// projectNumber passed in as URL variable may contain collection parent directories, need project number only to check access
			String hmmlProjNumDir = projectNumber.indexOf('/') != -1 ? projectNumber.substring(projectNumber.lastIndexOf('/') + 1) : projectNumber;
			hasAccess = readingRoomService.checkImageAccess(hmmlProjNumDir);
		}
		
		return hasAccess;
	}
	
	private boolean isExternalRequest(URL url, HttpServletRequest request) throws MalformedURLException {
		boolean isExternalRequest = false;		
		String host = url.getHost();
	    
	    if(!host.equalsIgnoreCase(request.getServerName())) {
	    	isExternalRequest = true;
	    }
	    
	    return isExternalRequest;
	}
}