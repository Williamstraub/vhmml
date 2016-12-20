package org.vhmml.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.repository.UserRepository;
import org.vhmml.web.VhmmlSession;

import com.google.common.collect.Lists;

public class VhmmlAuthSuccessHandler implements AuthenticationSuccessHandler {
	
	private AuthenticationSuccessHandler target = new SavedRequestAwareAuthenticationSuccessHandler();		
	
	@Autowired
	private UserRepository userRepository;
	
	public VhmmlAuthSuccessHandler() {
		super();
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		User user = (User)auth.getPrincipal();		
    	
        if (BooleanUtils.isTrue(user.getCredentialsExpired())) {
        	// if the user's credentials are expired we need to force them to change their password
        	// grab their username and put it on the session so we can get the user in the change password controller
        	// then log them out so they can't access anything        	
        	String username = user.getUsername();
        	new SecurityContextLogoutHandler().logout(request, response, auth);
        	VhmmlSession session = VhmmlSession.getSession(request);
        	session.setUsername(username);
        	session.setPasswordExpired(true);
            response.sendRedirect(request.getContextPath() + "/user/changePassword");
        } else {
        	User dbUser = userRepository.findByUsername(user.getUsername());
        	Date now = new Date();
        	
        	if(dbUser.getFirstLogin() == null) {
        		dbUser.setFirstLogin(now);
        	}
        	dbUser.setFailedLogins(0);
        	dbUser.setLastLogin(now);
        	// user may have been browsing reading room with an anonymous session, then decided to login in so they could view images
        	// save their search state so we can send them back to where they were with their search intact
        	VhmmlSession previousSession = VhmmlSession.getSession(request);
    		String destination = previousSession.getPostLoginDestination();
        	VhmmlSession session = VhmmlSession.getNewSession(request);
        	session.setSavedReadingRoomSearch(previousSession.getSavedReadingRoomSearch());
        	session.setAcceptedReadingRoomAgreement(previousSession.isAcceptedReadingRoomAgreement());
        	session.setUsername(user.getUsername());
        	
        	if(session.isAcceptedReadingRoomAgreement()) {        		
        		dbUser.setUsageAgreementAccepted(true);
        		dbUser.setUsageAgreementAcceptedDate(new Date());
        	} else if(user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER, Role.Name.ROLE_LEVEL_II_CATALOGER, Role.Name.ROLE_LEVEL_III_CATALOGER))) {
        		// admins & catalogers don't have to accept the agreement
        		session.setAcceptedReadingRoomAgreement(true);
        	}
        	
        	userRepository.save(dbUser);        	
			
        	setSessionTimeout(auth, request.getSession());
        	
        	if(StringUtils.isNotEmpty(destination)) {     
        		response.sendRedirect(request.getContextPath() + destination);
        	} else {
        		target.onAuthenticationSuccess(request, response, auth);
        	}         
        }
	}
	
	private void setSessionTimeout(Authentication auth, HttpSession session) {
		Object principal = auth.getPrincipal();	       
		 
		 if(principal.getClass().isAssignableFrom(User.class)) {			 
			 User user = (User)principal;
			 boolean isNoTimeoutRole = user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER, Role.Name.ROLE_LEVEL_II_CATALOGER, Role.Name.ROLE_LEVEL_III_CATALOGER));
			 
			 if(isNoTimeoutRole) {
				 // admin & cataloger timeouts are 4 hours
				 session.setMaxInactiveInterval(60 * 60 * 4);
			 }			 
		 }
	}
}
