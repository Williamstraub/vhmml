package org.vhmml.service;

import java.io.IOException;

public interface ImageService {	
	
	public static final String IMAGE_VIEW_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss z";
	
	public String getImageServerHost();
	public byte[] getThumbnail(String objectType, String projectNumber, String imageName) throws IOException;
	public byte[] getThumbnail(String objectType, String projectNumber, String imageName, Integer width) throws IOException;	
	public String getImagesPath(String objectType, String projectNumber) throws IOException;	
	public String getImageList(String objectType, String projectNumber) throws IOException;
	public byte[] getImage(String objectType, String projectNumber, String imageName, String region, String size, String rotation, String qualityAndFormat) throws IOException;
	public String getExternalIIIFData(String url) throws IOException;
	public String getIIIFManifest(String objectType, Long readingRoomObjectId) throws IOException;	
	public String getImageInfo(String objectType, String projectPath, String imageName) throws IOException;
}
