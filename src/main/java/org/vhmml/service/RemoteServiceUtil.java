package org.vhmml.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vhmml.service.ApplicationConfigService.Property;

@Component
public class RemoteServiceUtil {

	private static final Logger LOG = Logger.getLogger(RemoteServiceUtil.class);
	
	@Autowired
	private ApplicationConfigService configService;
	
	public HttpClient getHttpClient(boolean proxy) {
		HttpClient httpClient = new HttpClient();
		HttpClientParams params = httpClient.getParams();
		params.setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36");				
		String activeProfile = System.getProperty("spring.profiles.active");
		
		if(proxy && StringUtils.isNotEmpty(activeProfile) && !"local".equalsIgnoreCase(activeProfile)) {		
			String webServiceProxyServer = configService.getValue(Property.WEB_SERVICE_PROXY_SERVER);
			Integer webServiceProxyPort = configService.getIntValue(Property.WEB_SERVICE_PROXY_PORT);
			
			if(StringUtils.isNotEmpty(webServiceProxyServer) && webServiceProxyPort != null) {
				httpClient.getHostConfiguration().setProxy(webServiceProxyServer, webServiceProxyPort);
			}			
		}
		
		return httpClient;
	}
	
	public String executeGet(String url, boolean proxyRequest) throws IOException {
		return executeGet(url, null, proxyRequest);
	}
	
	public String executeGet(String url, Map<String, String> headers, boolean proxyRequest) throws IOException {
		String responseString = null;					
		HttpClient httpClient = getHttpClient(proxyRequest);		
		GetMethod get = new GetMethod(url);
		
		if(MapUtils.isNotEmpty(headers)) {			
			
			for(String header : headers.keySet()) {
				get.setRequestHeader(header, headers.get(header));
			}
		}
		
		try {
			httpClient.executeMethod(get);				
			int responseCode = get.getStatusCode();
			InputStream response = get.getResponseBodyAsStream();
			LOG.info("response code from get request " + responseCode + ", content length = " + get.getResponseContentLength());
			printHeaders(get);
			
			if (responseCode != HttpStatus.SC_OK) {
				String message = "Wrong response attempting to execute get at URL [" + url + "], expected " + HttpStatus.SC_OK + " but received " + responseCode;
				LOG.error(message);				
				throw new RuntimeException(message);
			} else {
				BufferedInputStream inputStream = new BufferedInputStream(response);
				responseString = IOUtils.toString(inputStream, "utf-8");
				inputStream.close(); 						
			}		
		} finally {
			get.releaseConnection();
		}	
		
		return responseString;
	}
	
	public String executeGet(String host, String port, String url, boolean proxyRequest) throws IOException {
		return executeGet("http://" + host + ":" + port + url, null, proxyRequest);		
	}
	
	public byte[] executeGetImage(String host, String port, String url) throws IOException {
		byte[] imageBytes = null;				
		// we don't proxy image requests because they're on the same domain
		HttpClient httpClient = getHttpClient(false);
		String imageUrl = "http://" + host + ":" + port + "/" + url;
		LOG.info("retriving image from image server at url " + imageUrl);
		GetMethod get = new GetMethod(imageUrl);
		
		try {
			httpClient.executeMethod(get);
			int responseCode = get.getStatusCode();
			imageBytes = get.getResponseBody();
			LOG.info("response code from get image request " + responseCode + ", content length = " + get.getResponseContentLength());
			printHeaders(get);
			
			if (responseCode != HttpStatus.SC_OK) {
				String message = "Wrong response attempting to retrieve image from image server at URL [" + imageUrl + "], expected " + HttpStatus.SC_OK + " but received " + responseCode;
				LOG.warn(message);
				throw new RuntimeException(message);
			} 				
		} finally {
			get.releaseConnection();
		}
		
		return imageBytes;
	}
	
	public HttpClient getHttpClient() {		
		return getHttpClient(true);
	}
	
	public void printHeaders(HttpMethodBase requestMethod) {		
		printHeaders("=== REQUEST HEADERS ===", requestMethod.getRequestHeaders());		
		printHeaders("=== RESPONSE HEADERS ===", requestMethod.getResponseHeaders());		
	}
	
	private void printHeaders(String label, Header[] headers) {
		LOG.info(label);		
		
		for(Header header : headers) {
			LOG.info(header.getName() + " = " + header.getValue());
		}
	}
}
