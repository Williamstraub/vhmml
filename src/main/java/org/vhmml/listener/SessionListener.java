package org.vhmml.listener;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vhmml.service.ReadingRoomService;
import org.vhmml.web.VhmmlSession;

public class SessionListener implements HttpSessionListener {

	public static final String APP_ATT_ACTIVE_SESSIONS = "activeSessions";
	
	private static final Logger LOG = Logger.getLogger(SessionListener.class);
	
	private boolean initialized;
	
	@Autowired	
	private ReadingRoomService readingRoomService;
	
	private static int activeSessions;

	public static int getActiveSession() {
		return activeSessions;
	}	

	public void sessionCreated(HttpSessionEvent sessionEvent) {
		
		// spring-ify the bean when the first session is created		
		if(!initialized) {
			init(sessionEvent.getSession().getServletContext());
		}
		
		activeSessions++;
		sessionEvent.getSession().getServletContext().setAttribute(APP_ATT_ACTIVE_SESSIONS, activeSessions);
		LOG.info("Session created, active session count is " + activeSessions);
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		activeSessions--;
		HttpSession session = sessionEvent.getSession();
		Object vhmmlSessionObject = session.getAttribute(VhmmlSession.PARAM_SESSION);
		
		if(vhmmlSessionObject != null) {
			VhmmlSession vhmmlSession = (VhmmlSession)vhmmlSessionObject;
			String username = vhmmlSession.getUsername();
			
			if(StringUtils.isNotEmpty(username)) {
				try {
					readingRoomService.clearLockedRecordsForUser(username);
				} catch(IOException e) {
					LOG.error("IOException attempting to clear record locks after session termination for user " + username, e);
				}				
			}
		}
		
		session.getServletContext().setAttribute(APP_ATT_ACTIVE_SESSIONS, activeSessions);
		LOG.info("Session destroyed, active session count is " + activeSessions);
	}

	private void init(ServletContext servletContext) {	
		
		if(!this.initialized) {
			WebApplicationContextUtils
	        .getRequiredWebApplicationContext(servletContext)
	        .getAutowireCapableBeanFactory()
	        .autowireBean(this);
		
			this.initialized = true;
		}		
	}
}
