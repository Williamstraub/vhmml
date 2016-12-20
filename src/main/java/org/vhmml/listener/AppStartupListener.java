package org.vhmml.listener;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.exception.UserExistsException;
import org.vhmml.service.ApplicationConfigService;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.ElasticSearchServiceImpl;
import org.vhmml.service.ReadingRoomService;
import org.vhmml.service.UserService;

public class AppStartupListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(AppStartupListener.class);	
	public static final String APP_ATT_APP_NAME = "applicationName";
	public static final String APP_ATT_APP_VERSION = "applicationVersion";
	public static final String APP_ATT_ENVIRONMENT = "vhmmlEnvironment";
	public static final String APP_ATT_ROLE_NAMES = "roleNames";
	public static final String APP_ATT_CONFIG_VALUES = "configValues";

	@Autowired
	private ElasticSearchService elasticSearchService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReadingRoomService readingRoomService;
	
	@Autowired
	private ApplicationConfigService appConfigService;
	
	public AppStartupListener() {
		super();
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// context destroyed code goes here
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {		
		ServletContext servletContext = servletContextEvent.getServletContext();
		
		WebApplicationContextUtils
	        .getRequiredWebApplicationContext(servletContext)
	        .getAutowireCapableBeanFactory()
	        .autowireBean(this);
		
		setEnvironmentOnServletContext(servletContext);
		createInitalUser();
		initializeElasticSearch(servletContext);
		putRolesOnServletContext(servletContext);
		putConfigValuesOnServletContext(servletContext);
		
		try {
			// need to clear any orphaned record locks on startup
			readingRoomService.clearLockedRecords();
		} catch(IOException e) {
			LOG.error("IOException attempting to clear record locks on server startup, there may be orphaned record locks in the database", e);
		}		
	}
	
	private void putRolesOnServletContext(ServletContext servletContext) {
		StringBuilder roleNamesString = new StringBuilder();
		Role.Name[] roleNames = Role.Name.values();
		
		for(int i = 0; i < roleNames.length; i++) {			
			if(i > 0) {
				roleNamesString.append(",");
			}
			roleNamesString.append(roleNames[i].name());
		}
						
		servletContext.setAttribute(APP_ATT_ROLE_NAMES, roleNamesString.toString());
	}
	
	private void putConfigValuesOnServletContext(ServletContext servletContext) {					
		servletContext.setAttribute(APP_ATT_CONFIG_VALUES, appConfigService.getAllAsMap());
	}
	
	private void initializeElasticSearch(ServletContext servletContext) {
		try {			
			elasticSearchService.initializeIndex(ElasticSearchServiceImpl.Index.SEARCH);
			elasticSearchService.initializeIndex(ElasticSearchServiceImpl.Index.REPORT);
		} catch(Exception e) {
			LOG.error("Unexpected Exception while initializing serach index in AppStartupListener, search index may not be initialized.", e);
		}	
	}
	
	private void createInitalUser() {
		User adminUser = userService.findByUsername("admin");
		
		// if the admin user doesn't exist, we create it with no roles and a "known" password that must be changed
		// this allows deployments to a new environment to have an initial user that can log in and change the password
		// note that the user will not have any roles and will therefore not be able to do anything, the roles
		// have to be manually added by someone who has access to the database, this ensures that if the admin user 
		// is compromised because someone has access to the "known" initial password, the user will have no rights
		if(adminUser == null) {
			adminUser = new User();
			adminUser.setUsername("admin");
			adminUser.setPassword("changeme");
			adminUser.setCredentialsExpired(true);
			adminUser.setFirstName("Admin");
			
			try {
				userService.createUser(adminUser, null);
			} catch(UserExistsException e) {
				LOG.error("Unexpected UserExistsException trying to create initial user", e);
			}			
		}
	}
	
	private void setEnvironmentOnServletContext(ServletContext servletContext) {
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		List<String> arguments = runtimeMxBean.getInputArguments();
		
		for(String arg : arguments) {
			if(arg.startsWith("-Dspring.profiles.active")) {
				servletContext.setAttribute(APP_ATT_ENVIRONMENT, arg.substring(arg.indexOf("=") + 1));
			}			
		}
		
		// grab the app name & svn version from the manifest and put them on the servlet context so we can display them on the admin page
		try {
			InputStream inputStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
			
			if(inputStream != null) {			
				Manifest manifest = new Manifest(inputStream);			
				Attributes atts = manifest.getMainAttributes();	
				String appName = atts.getValue("Specification-Title").toString();
				String appVersion = atts.getValue("Implementation-Version").toString();
				LOG.info("Starting " + appName + " version: " + appVersion);
				// NOTE: we use different names for these attributes than what Maven puts in the manifest because we can't use names like 
				// Specification-Title in an EL expression on a JSP page, the '-' symbol will cause the JSP interpreter to do subtraction on it
				servletContext.setAttribute(APP_ATT_APP_NAME, appName);
				servletContext.setAttribute(APP_ATT_APP_VERSION, appVersion);	
			}
					
		} catch(IOException e) {
			LOG.error("Unable to load manifest, version information will not be available", e);
		}	
	}	
}
