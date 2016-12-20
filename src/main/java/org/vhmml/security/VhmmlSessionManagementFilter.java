package org.vhmml.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;

public class VhmmlSessionManagementFilter extends SessionManagementFilter {

	@Autowired
	private WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator;	
	
	public VhmmlSessionManagementFilter(SecurityContextRepository securityContextRepository) {
		super(securityContextRepository);
	}

	public VhmmlSessionManagementFilter(SecurityContextRepository securityContextRepository, SessionAuthenticationStrategy sessionStrategy) {
		super(securityContextRepository, sessionStrategy);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isUserAllowedAccess = webInvocationPrivilegeEvaluator.isAllowed(request.getServletPath(), auth);
		
		// regular session management filter will send you to the session timeout url even if you request an unprotected
		// resource with an invalid session id
		if(isUserAllowedAccess) {
			chain.doFilter(req, res);
		} else {			
			// let the regular session management filter handle sending them a redirect to the invalid session page
			super.doFilter(req, res, chain);
		}	
	}
}
