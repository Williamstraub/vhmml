package org.vhmml.controller.helper;

import java.io.IOException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.controller.BaseValidationController;
import org.vhmml.controller.ControllerConstants;
import org.vhmml.controller.UserController;
import org.vhmml.entity.User;
import org.vhmml.exception.UserExistsException;
import org.vhmml.form.RegistrationForm;
import org.vhmml.service.UserService;
import org.vhmml.web.VhmmlMessage;
import org.vhmml.web.VhmmlMessage.Severity;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This helper class exists to encapsulate the logic for saving user account information that is 
 * used by both the UserController when users save their own account information and also by the
 * AdminController when an administrator saves account information for other users.
 *
 */
@Component
public class UserControllerHelper extends BaseValidationController {

	public static final String REQUEST_ATT_USER = "user";			
	public static final String REQUEST_ATT_SAVE_ACCOUNT_SETTINGS_ACTION = "saveAccountSettingsAction";
	public static final String REQUEST_ATT_REGISTRATION_FORM = "registrationForm";
	
	public enum SaveAction {
		REGISTER("register"),
		SAVE_ACCOUNT_SETTINGS("accountSettings"),
		ADMIN_SAVE_ACCOUNT_SETTINGS("adminAccountSettings");
		
		private String name;
		
		SaveAction(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	@Autowired
	private UserService userService;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public UserControllerHelper() {
		super();
	}
	
	public ModelAndView getAccountSettingsView(SaveAction saveAction, User user) {
		ModelAndView modelAndView = new ModelAndView(UserController.VIEW_ACCOUNT_SETTINGS);
		RegistrationForm registrationForm = new RegistrationForm();
		
		if(user != null) {
			BeanUtils.copyProperties(user, registrationForm);
		}
		
		modelAndView.addObject(REQUEST_ATT_REGISTRATION_FORM, registrationForm);
		modelAndView.addObject(REQUEST_ATT_SAVE_ACCOUNT_SETTINGS_ACTION, saveAction.getName());

		return modelAndView;
	}
	
	public ModelAndView saveAccountSettings(RegistrationForm registrationForm, BindingResult result, String successView) throws IOException {
		ModelAndView modelAndView = new ModelAndView(UserController.VIEW_ACCOUNT_SETTINGS);		
		boolean errors = result.hasErrors();
		
		if (!errors) {
			User user = new User();
			BeanUtils.copyProperties(registrationForm, user);			
			
			try {
				user = userService.saveUser(user);
				User authenticatedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				// if the logged in user is updating their own settings, copy the changes to the auth object so they're reflected immediately
				if(user.getId().equals(authenticatedUser.getId())) {
					BeanUtils.copyProperties(user, authenticatedUser);
				}
								
				modelAndView = new ModelAndView(successView);
				modelAndView.addObject(REQUEST_ATT_USER, user);
				VhmmlMessage message = new VhmmlMessage("Account settings saved successfully.");
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
			} catch(UserExistsException e) {
				result.rejectValue("username", null, e.getMessage());
				errors = true;				
			} catch(Exception e) {
				VhmmlMessage message = new VhmmlMessage("Save account settings failed, an unexpected error occurred. Please contact the system administrator if this error continues to occur.", Severity.ERROR);
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
				errors = true;				
			}			
		} 
		
		if(errors) {			
			modelAndView.addObject(REQUEST_ATT_SAVE_ACCOUNT_SETTINGS_ACTION, "accountSettings");
			modelAndView.addObject(REQUEST_ATT_REGISTRATION_FORM, registrationForm);
			addFieldErrorsToModel(modelAndView, result);
		}
		
		return modelAndView;
	}
}
