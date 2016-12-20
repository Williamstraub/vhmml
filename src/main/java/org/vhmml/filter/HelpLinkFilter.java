package org.vhmml.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

public class HelpLinkFilter implements Filter {
	
	public static final String PARAM_HELP_LINK = "pageHelpLink";
	
	private static Map<String, String> customHelpLinks = new HashMap<String, String>();
	
	
	public HelpLinkFilter() {
		super();
	}

	public void destroy() {
		// destroy code goes here
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		String contextPath = httpRequest.getContextPath();
		String requestPath = httpRequest.getServletPath();
		String helpLink = getHelpLink(requestPath);
		helpLink = helpLink == null ? contextPath + "/help" : contextPath + helpLink;
		httpRequest.setAttribute(PARAM_HELP_LINK, helpLink);		
		httpRequest.setAttribute("pageUrl", contextPath + requestPath);		
		httpRequest.setAttribute("breadcrumbs", getBreadcrumbs(contextPath, requestPath));
		
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		customHelpLinks.put("/lexicon", "/lexicon/help");
		customHelpLinks.put("/reference", "/reference/help");
		customHelpLinks.put("/school", "/school/help");
		customHelpLinks.put("/readingRoom", "/readingRoom/help");
		customHelpLinks.put("/folio", "/folio/help");
		customHelpLinks.put("/scriptorium", "/scriptorium/help");
		customHelpLinks.put("/registration", "/registration/help");
		customHelpLinks.put("/login", "/login/help");	
		customHelpLinks.put("/catalogDatabase", "/catalogDatabase/help");
	}
	
	private String getHelpLink(String requestPath) {
		String helpLink = null;				
		Set<String> customLinkPaths = customHelpLinks.keySet();
		
		for(String customLinkPath : customLinkPaths) {
			if(requestPath.startsWith(customLinkPath)) {
				helpLink = customHelpLinks.get(customLinkPath);
				break;
			}
		}
		
		return helpLink;
	}
	
	private Map<String, String> getBreadcrumbs(String contextPath, String requestPath) {
		Map<String, String> breadcrumbs = new LinkedHashMap<String, String>();
				
		// remove starting & trailing slashes, then split the request on slashes to get the bread crumbs
		requestPath = StringUtils.removeEnd(StringUtils.removeStart(requestPath, "/"), "/");		
		String[] segments = requestPath.split("/");
		StringBuilder url = new StringBuilder();
		url.append(contextPath);
		
		if(contextPath.isEmpty()) {
			breadcrumbs.put("Home", "/");
		} else {
			breadcrumbs.put("Home", url.toString());
		}				
		
		for(int i = 0; i < segments.length; i++) {
			
			// just doing home & main category for now until we have time to figure out a good way
			// to handle directory paths in static page locations like school/lesson/basics-paleography
			if(i >= 1) {
				break;
			} else {
				String segment = segments[i].trim();
				
				if(segment.length() > 0) {
					String name = WordUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(segment), ' '));
					url.append("/").append(segment);				
					breadcrumbs.put(name, url.toString());				
				}
			}					
		}
		
		return breadcrumbs;
	}
}
