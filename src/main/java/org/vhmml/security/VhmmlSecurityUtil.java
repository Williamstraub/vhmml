package org.vhmml.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vhmml.entity.Role;
import org.vhmml.entity.User;

import com.google.common.collect.Lists;

public class VhmmlSecurityUtil {

	private VhmmlSecurityUtil() {
		super();
	}
	
	public static User getCurrentUser() {
		User user = null;
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object principal = securityContext.getAuthentication().getPrincipal();		
		
		if(principal.getClass().isAssignableFrom(User.class)) {
			user = ((User)principal);		
		}
		
		return user;
	}
	
	public static boolean isCurrentUserCataloger() {
		boolean isCataloger = false;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object principal = securityContext.getAuthentication().getPrincipal();		
		
		if(principal.getClass().isAssignableFrom(User.class)) {
			User user = (User)principal;
			isCataloger = user.hasRole(Lists.newArrayList(Role.Name.ROLE_ADMIN, Role.Name.ROLE_LEVEL_I_CATALOGER, Role.Name.ROLE_LEVEL_II_CATALOGER, Role.Name.ROLE_LEVEL_III_CATALOGER));
		}
		
		return isCataloger;
	}
	
	public static boolean isCurrentScholar() {
		boolean isScholar = false;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Object principal = securityContext.getAuthentication().getPrincipal();		
		
		if(principal.getClass().isAssignableFrom(User.class)) {
			User user = (User)principal;
			isScholar = user.hasRole(Role.Name.ROLE_SCHOLAR);
		}
		
		return isScholar;
	}
}
