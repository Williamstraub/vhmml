package org.vhmml.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.FolioObject;
import org.vhmml.entity.User;
import org.vhmml.entity.readingroom.City;
import org.vhmml.entity.readingroom.Country;
import org.vhmml.entity.readingroom.ReadingRoomObject;
import org.vhmml.entity.readingroom.Repository;
import org.vhmml.repository.FolioObjectRepository;
import org.vhmml.repository.readingroom.ReadingRoomObjectRepository;
import org.vhmml.security.VhmmlSecurityUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ImageServiceImpl implements ImageService {
	
	private static final Logger LOG = Logger.getLogger(ImageServiceImpl.class);	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private RemoteServiceUtil remoteServiceUtil;
	
	@Autowired
	private ApplicationConfigService configService;
	
	@Autowired
	private ReadingRoomObjectRepository readingRoomObjectRepository;
	
	@Autowired
	private FolioObjectRepository folioObjectRepository;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@Value("${image.request.url}")
	private String imageRequestUrl;
	
	@Value("${image.server.host}")
	private String imageServerHost;
	
	@Value("${image.server.port}")
	private String imageServerPort;
	
	@Value("${iiif.server.host}")
	private String iiifServerHost;
	
	@Value("${iiif.server.port}")
	private String iiifServerPort;	
	
	@Value("${image.server.request.url}")
	private String imageServerRequestUrl;
	
	@Value("${vhmml.protocol}")
	private String vhmmlProtocol;
	
	@Value("${vhmml.host.override}")
	private String vhmmlHostOverride;
	
	public String getImageServerHost() {
		return imageServerHost;
	}
	
	public byte[] getThumbnail(String objectType, String projectNumber, String imageName) throws IOException {
		return getThumbnail(objectType, projectNumber, imageName, 200);
	}
	
	public byte[] getThumbnail(String objectType, String projectNumber, String imageName, Integer width) throws IOException {
		// size parameter can take several forms using width, height or both. We are using
		// the format !width,height, which will maintain the aspect ratio but bring back an image the width
		// the user requested or smaller, and also confine the image to our max height
		// for more information see http://iiif.io/api/image/2.0/#size
		
		String widthString = width != null ? width.toString() + "," : "200,";
		String maxHeight = configService.getValue(ApplicationConfigService.Property.MAX_THUMBNAIL_HEIGHT);
		String size = "!" + widthString + maxHeight;
		return getImage(objectType, projectNumber, imageName, "full", size, "0", "native.jpg");
	}
	
	public String getImagesPath(String objectType, String projectNumber) throws IOException {
		String imagesPath = null;		
		
		if(StringUtils.isNotEmpty(projectNumber)) {
			
			if("FOLIO".equalsIgnoreCase(objectType)) {
				imagesPath = "FOLIO/" + projectNumber;
			} else {
				ReadingRoomObject readingRoomObject = readingRoomObjectRepository.findByHmmlProjectNumber(projectNumber);
				
				if(readingRoomObject != null) {
					
					imagesPath = readingRoomObject.getImagesLocation();
					
					if(StringUtils.isEmpty(imagesPath)) {
						imagesPath = remoteServiceUtil.executeGet(iiifServerHost, iiifServerPort, "/iiif-service/imagesPath/" + UriUtils.encodePath(projectNumber, "utf-8"), false);
						
						if(StringUtils.isNotEmpty(imagesPath)) {
							imagesPath = objectMapper.readValue(imagesPath, String.class);
							readingRoomObject.setImagesLocation(imagesPath);
							readingRoomObjectRepository.save(readingRoomObject);
						}					
					}				
				}
			}				
		}		
		
		return imagesPath;
	}
	
	public String getImageList(String objectType, String projectNumber) throws IOException {
		String imageListJson = "";
		projectNumber = projectNumber.trim();
		
		if(StringUtils.isNotEmpty(projectNumber)) {
					
			String imagesPath = UriUtils.encodePath(getImagesPath(objectType, projectNumber), "utf-8");
			
			if(StringUtils.isNotEmpty(imagesPath)) {
				imageListJson = remoteServiceUtil.executeGet(iiifServerHost, iiifServerPort, "/iiif-service/imageList/" + imagesPath, false);
			}				
		}		
		
		return imageListJson;
	}

	public byte[] getImage(String objectType, String projectNumber, String imageName, String region, String size, String rotation, String qualityAndFormat) throws IOException {		
		byte[] imageBytes = null;
		
		if(StringUtils.isNotEmpty(objectType) && StringUtils.isNotEmpty(projectNumber) && StringUtils.isNotEmpty(imageName)) {
			// object type is something like READING_ROOM or FOLIO
			// so complete path is something like READING_ROOM/GARZ/GARZ 00005
			String imagesPath = getImagesPath(objectType, projectNumber);
			String projectPath = UriUtils.encodePath(imagesPath, "utf-8").replaceAll("/", "%2F");
			imageName = UriUtils.encodePath(imageName, "utf-8");
			String imageUrl = imageServerRequestUrl + "/" + projectPath + "%2F" + imageName + "/" + region + "/" + size + "/" + rotation + "/" + qualityAndFormat;
			imageBytes = remoteServiceUtil.executeGetImage(imageServerHost, imageServerPort, imageUrl);					
		}	
		
		return imageBytes;
	}
	
	public String getIIIFManifest(String objectType, Long objectId) throws IOException {
		Date start = new Date();
		String manifestJson = null;
		String projectNumber = null;
		List<Map<String, String>> metaData = null;
		boolean rightToLeft = false;
		
		if("FOLIO".equalsIgnoreCase(objectType)) {
			FolioObject folioObject = folioObjectRepository.findOne(objectId);
			projectNumber = folioObject.getFolioObjectNumber();
			metaData = getMetaData(folioObject);
		} else {
			ReadingRoomObject readingRoomObject = readingRoomObjectRepository.findOne(objectId);			
			trackImageViewingData(readingRoomObject);
			projectNumber = readingRoomObject.getHmmlProjectNumber();
			rightToLeft = readingRoomObject.isRightToLeft();
			metaData = getMetaData(readingRoomObject);
		}
		
		if(StringUtils.isNotEmpty(projectNumber)) {			
			HttpClient httpClient = remoteServiceUtil.getHttpClient(false);
			String manifestUrl = "http://" + iiifServerHost + ":" + iiifServerPort + "/iiif-service/manifest/" + UriUtils.encodePath(projectNumber, "utf-8");		
			PostMethod post = new PostMethod(manifestUrl);
			Map<String, Object> params = new HashMap<>();
			
			params.put("label", projectNumber);
			params.put("metaData", metaData);
//			We don't currently have a description so we're removing it from the manifest so it doesn't show in Mirador's info
//			params.put("description", shelfMark);
			params.put("attribution", "Provided by the Hill Museum & Manuscript Library");
			params.put("viewingDirection", rightToLeft ? "right-to-left" : "left-to-right");
			StringRequestEntity requestBody = new StringRequestEntity(
				objectMapper.writeValueAsString(params),
				"application/json",
				"UTF-8"
			);

			post.setRequestEntity(requestBody);			
			
			try {
				httpClient.executeMethod(post);			
				int responseCode = post.getStatusCode();
				InputStream response = post.getResponseBodyAsStream();
				
				if (responseCode != HttpStatus.SC_OK) {
					String message = "Wrong response attempting to retrieve IIIF manifest from IIIF service at URL [" + manifestUrl + "], expected " + HttpStatus.SC_OK + " but received " + responseCode;
					LOG.error(message);
					throw new RuntimeException(message);
				} else {
					BufferedInputStream inputStream = new BufferedInputStream(response);
					manifestJson = IOUtils.toString(inputStream, "utf-8");
					inputStream.close(); 						
				}	
			} finally {
				post.releaseConnection();
			}
		}
		
		LOG.info("get manifest took " + (new Date().getTime() - start.getTime())/1000);
		
		return manifestJson;
	}
	
	public String getExternalIIIFData(String url) throws IOException {		
		String jsonData = null;				
		
		if(StringUtils.isNotEmpty(url)) {
			jsonData = remoteServiceUtil.executeGet(url, true);							
			
			try {
				objectMapper.readTree(jsonData);
			} catch(JsonProcessingException e) {
				LOG.warn("Invalid JSON received from external url: " + url, e);
			}			
		}		
		
		return jsonData;
	}
	
	public String getImageInfo(String objectType, String projectPath, String imageName) throws IOException {
		String imageInfoJson = null;
		
		if(StringUtils.isNotEmpty(projectPath) && StringUtils.isNotEmpty(imageName)) {
			String imageHostAndPort = imageServerHost + ":" + imageServerPort;
			String digilibImageRequest = "http://" + imageHostAndPort + "/digilib/Scaler/IIIF";
			// Currently the test server has 2 domain names registered and InetAddress.getLocalHost().getHostName() returns the wrong one (vhmmltestweb)
			// so we have this vhmmlHostOverride hack to get around that until the host name registration gets fixed by IT, it's configured in test.database.properties.
			// In general, the InetAddress.getLocalHost().getHostName() call has proved to be problematic because we're relying on it to return the same
			// host name that is used by Mirador to request images, but this is often not the case.  For example, even on local this call might return something
			// like chad-laptop on a windows machine, when you're most likely accessing the site using localhost.
			String vhmmlHost = StringUtils.isNotEmpty(vhmmlHostOverride) ? vhmmlHostOverride : InetAddress.getLocalHost().getHostName();
			int vhmmlPort = ServletUriComponentsBuilder.fromCurrentContextPath().build().getPort();			
			String vhmmlHostAndPort = vhmmlHost + (vhmmlPort > 0 ? ":" + vhmmlPort : "");
			String vhmmlImageRequest = vhmmlProtocol + "://" + vhmmlHostAndPort + imageRequestUrl;
			// objectType is something like READING_ROOM or FOLIO, project path is something like GARZ/GARZ 00005
			projectPath = objectType + "/" + projectPath;
			String utf8ProjectPath = UriUtils.encodePath(projectPath, "utf-8");
			String utf8ImageName = UriUtils.encodePath(imageName, "utf-8");
			String imagePath = utf8ProjectPath + "/" + utf8ImageName;
			String encodedImagePath = imagePath.replaceAll("/", "%2F");			
			String imageUrl = "http://" + imageHostAndPort + "/" + imageServerRequestUrl + "/" + encodedImagePath;
			String imageInfoUrl = imageUrl + "/info.json";
						
			HttpClient httpClient = remoteServiceUtil.getHttpClient(false);
			LOG.info("retriving image info from image server at url " + imageInfoUrl);			
			GetMethod get = new GetMethod(imageInfoUrl);
			
			try {
				httpClient.executeMethod(get);
				int responseCode = get.getStatusCode();
				imageInfoJson = get.getResponseBodyAsString();
				
				if(StringUtils.isNotEmpty(imageInfoJson)) {
					// we need to route all image requests through the vhmml app so we can apply our security to requests
					// so replace digilib image urls in the info.json response with vhmml image urls
					imageInfoJson = StringUtils.replace(imageInfoJson, digilibImageRequest, vhmmlImageRequest);
					
					// if we're over https we're proxying request so we don't want the paths encoded in the image info JSON
					if("https".equals(vhmmlProtocol)) {
						imageInfoJson = StringUtils.replace(imageInfoJson, encodedImagePath, imagePath);
					}
				}
				LOG.info("response code from get image info request " + responseCode + ", content length = " + get.getResponseContentLength());
				remoteServiceUtil.printHeaders(get);
				
				if (responseCode != HttpStatus.SC_OK) {
					String message = "Wrong response attempting to retrieve image info from image server at URL [" + imageInfoUrl + "], expected " + HttpStatus.SC_OK + " but received " + responseCode;
					LOG.warn(message);
					throw new RuntimeException(message);
				}
			} finally {
				get.releaseConnection();
			}			
		}
		
		return imageInfoJson;
	}
	
	private List<Map<String, String>> getMetaData(ReadingRoomObject object) {
		List<Map<String, String>> metaData = new ArrayList<>();
		
		metaData.add(getMetaDataProp("Country", object.getCountry().getName()));
		metaData.add(getMetaDataProp("City", object.getCity().getName()));
		metaData.add(getMetaDataProp("Repository", object.getRepository().getName()));
		metaData.add(getMetaDataProp("Shelfmark", object.getShelfMark()));
		metaData.add(getMetaDataProp("HMML Project number", object.getHmmlProjectNumber()));
		metaData.add(getMetaDataProp("Rights link", "https://www.vhmml.org/terms"));
		String permalinkUrl = configService.getValue(ApplicationConfigService.Property.PERMALINK_URL);
		metaData.add(getMetaDataProp("Permalink", "https://" + permalinkUrl + "/readingRoom/view/" + object.getId()));
				
		return metaData;
	}
	
	private List<Map<String, String>> getMetaData(FolioObject object) {
		List<Map<String, String>> metaData = new ArrayList<>();
		
		metaData.add(getMetaDataProp("Country", object.getCountry()));
		metaData.add(getMetaDataProp("City", object.getCity()));
		metaData.add(getMetaDataProp("Repository", object.getRepository()));
		metaData.add(getMetaDataProp("Shelfmark", object.getShelfMark()));
		metaData.add(getMetaDataProp("Project number", object.getFolioObjectNumber()));
		metaData.add(getMetaDataProp("Rights link", "https://www.vhmml.org/terms"));
		String permalinkUrl = configService.getValue(ApplicationConfigService.Property.PERMALINK_URL);
		metaData.add(getMetaDataProp("Permalink", "https://" + permalinkUrl + "/folio/view/" + object.getId()));
				
		return metaData;
	}
	
	private Map<String, String> getMetaDataProp(String label, String value) {
		Map<String, String> prop = new HashMap<>();
		prop.put("label", label);
		prop.put("value", value);
		
		return prop;
	}
	
	private void trackImageViewingData(ReadingRoomObject object) {
		User user = VhmmlSecurityUtil.getCurrentUser();
		String username = user == null ? "unregistered" : user.getUsername();
		
		try {
			Map<String, String> imageViewData = new HashMap<>();
			Country country = object.getCountry();
			City city = object.getCity();
			Repository repository = object.getRepository();			
			imageViewData.put("username", username);
			imageViewData.put("hmmlProjectNumber", object.getHmmlProjectNumber());
			imageViewData.put("collection", object.getCollection());		
			imageViewData.put("country", country != null ? country.getName() : "");
			imageViewData.put("city", city != null ? city.getName() : "");
			imageViewData.put("repository", repository != null ? repository.getName() : "");
			imageViewData.put("date", new SimpleDateFormat(IMAGE_VIEW_DATE_FORMAT).format(new Date()));
			elasticSearchService.updateIndexedObject(IndexedType.IMAGE_VIEW, null, objectMapper.writeValueAsString(imageViewData));
		} catch(Exception e) {
			LOG.error("Unexpected exception attempting to track image viewing statistics for object " + object + ", and user " + username, e);
		}		
	}

	public String getIiifServerHost() {
		return iiifServerHost;
	}

	public void setIiifServerHost(String iiifServerHost) {
		this.iiifServerHost = iiifServerHost;
	}

	public String getIiifServerPort() {
		return iiifServerPort;
	}

	public void setIiifServerPort(String iiifServerPort) {
		this.iiifServerPort = iiifServerPort;
	}
}
