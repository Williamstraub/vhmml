package org.vhmml.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AdminMenuInterceptor extends HandlerInterceptorAdapter {

	public static final String REQUEST_ATT_SELECTED_MENU_ITEM = "selectedMenuItem";
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute(REQUEST_ATT_SELECTED_MENU_ITEM, request.getParameter(REQUEST_ATT_SELECTED_MENU_ITEM));
		return true;
	}
}
