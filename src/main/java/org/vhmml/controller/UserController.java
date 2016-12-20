package org.vhmml.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.controller.helper.UserControllerHelper;
import org.vhmml.controller.helper.UserControllerHelper.SaveAction;
import org.vhmml.entity.User;
import org.vhmml.exception.AuthenticationFailedException;
import org.vhmml.exception.UserDisabledException;
import org.vhmml.form.ChangePasswordForm;
import org.vhmml.form.ForgotPasswordForm;
import org.vhmml.form.RegistrationForm;
import org.vhmml.service.UserService;
import org.vhmml.web.VhmmlMessage;
import org.vhmml.web.VhmmlMessage.Severity;
import org.vhmml.web.VhmmlMessageUtil;
import org.vhmml.web.VhmmlSession;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/user")
public class UserController extends BaseValidationController {
	
	public static final String VIEW_HOME = "static/home";	
	public static final String VIEW_ACCOUNT_SETTINGS = "user/account-settings";		
	public static final String VIEW_CHANGE_PASSWORD = "user/change-password";
	public static final String VIEW_FORGOT_PASSWORD = "user/forgot-password";
	
	public static final String REQUEST_ATT_CHANGE_PASSWORD_FORM = "changePasswordForm";
	public static final String REQUEST_ATT_FORGOT_PASSWORD_FORM = "forgotPasswordForm";
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private UserControllerHelper userControllerHelper;
	
	@Autowired
	private VhmmlMessageUtil messageUtil;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
		
	@RequestMapping(value = "/accountSettings", method = RequestMethod.GET)
	public ModelAndView viewAccountSettings() {				
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();		
		User user = (User)authentication.getPrincipal();		
		return userControllerHelper.getAccountSettingsView(SaveAction.SAVE_ACCOUNT_SETTINGS, user);
	}	
	
	@RequestMapping(value = "/accountSettings", method = RequestMethod.POST)
	public ModelAndView saveAccountSettings(@ModelAttribute @Valid RegistrationForm registrationForm, BindingResult result) throws IOException {
		// the user can't see or change internal notes, so we have to retrieve them and set them on the form so they don't get erased
		User user = userService.findById(registrationForm.getId());
		registrationForm.setInternalNotes(user.getInternalNotes());
		return userControllerHelper.saveAccountSettings(registrationForm, result, VIEW_HOME);			
	}	
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public ModelAndView viewChangePassword(HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_CHANGE_PASSWORD);		
		VhmmlSession session = VhmmlSession.getSession(request);
		
		if(session.isPasswordExpired()) {
			VhmmlMessage message = new VhmmlMessage("Your password has expired and must be changed before you can continue.", Severity.ERROR);
			modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
		}
		
		modelAndView.addObject(REQUEST_ATT_CHANGE_PASSWORD_FORM, new ChangePasswordForm());
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ModelAndView changePassword(@ModelAttribute @Valid ChangePasswordForm changePasswordForm, BindingResult result, HttpServletRequest request) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_CHANGE_PASSWORD);
		boolean errors = result.hasErrors();
		
		if(!errors) {
			try {
				String username = null;
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				
				if(auth.getPrincipal() instanceof User) {					
					User user = (User)auth.getPrincipal();
					username = user.getUsername();
				} else {
					// if the user isn't logged in, they may have an expired password, in that case the VhmmlAuthSuccessHandler
					// will have logged them out and put their username on the session so we can change their password
					VhmmlSession session = VhmmlSession.getSession(request);
					username = session.getUsername();					
				}					
				
				userService.changePassword(username, changePasswordForm.getCurrentPassword(), changePasswordForm.getNewPassword());
				VhmmlSession session = VhmmlSession.getSession(request);
				session.setPasswordExpired(false);
				modelAndView = new ModelAndView(VIEW_HOME);				
				VhmmlMessage message = new VhmmlMessage("Password changed successfully.");
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
			} catch(AuthenticationFailedException e) {
				result.rejectValue("currentPassword", null, e.getMessage());
				errors = true;
			}			
		}
		
		if(errors) {
			modelAndView.addObject(REQUEST_ATT_CHANGE_PASSWORD_FORM, changePasswordForm);
			addFieldErrorsToModel(modelAndView, result);
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView viewForgotPassword() {
		ModelAndView modelAndView = new ModelAndView(VIEW_FORGOT_PASSWORD);		
		modelAndView.addObject(REQUEST_ATT_FORGOT_PASSWORD_FORM, new ForgotPasswordForm());
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ModelAndView submitForgotPassword(@ModelAttribute @Valid ForgotPasswordForm forgotPasswordForm, BindingResult result) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_FORGOT_PASSWORD);
		boolean errors = result.hasErrors();
		
		if(!errors) {
			try {
				userService.sendTemporaryPassword(forgotPasswordForm.getEmailAddress());
				modelAndView = new ModelAndView(VIEW_HOME);
				VhmmlMessage message = new VhmmlMessage("A temporary password has been sent to your email address. Upon signing in with the temporary password you will be required to create a new password.");
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
			} catch(UsernameNotFoundException e) {
				result.rejectValue("emailAddress", null, e.getMessage());
				errors = true;
			} catch(UserDisabledException e) {
				VhmmlMessage message = new VhmmlMessage(e.getMessage(), Severity.WARN);
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
				errors = true;
			} catch(Exception e) {
				VhmmlMessage message = new VhmmlMessage("Password reset failed, an unexpected error has occurred. If the error persists please contact vHMML support.", Severity.ERROR);				
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
				errors = true;
			}
		}
		
		if(errors) {
			modelAndView.addObject(REQUEST_ATT_FORGOT_PASSWORD_FORM, forgotPasswordForm);
			addFieldErrorsToModel(modelAndView, result);
		}
				
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/changePassword/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getChangePasswordValidationRules() {		
		return super.getValidationRules(ChangePasswordForm.class);
	}
	
	@ResponseBody
	@RequestMapping(value = "/forgotPassword/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getForgotPasswordValidationRules() {		
		return super.getValidationRules(ForgotPasswordForm.class);
	}
	
	@ResponseBody 
	@RequestMapping("/validateSession")
	public boolean validateSession(HttpServletRequest request) {
		return request.isRequestedSessionIdValid();
	}
	
	@ResponseBody 
	@RequestMapping(value = "/acceptReadingRoomAgreement", method = RequestMethod.PUT)
	public boolean acceptReadingRoomAgreement(HttpServletRequest request) {
		VhmmlSession session = VhmmlSession.getSession(request);
		session.setAcceptedReadingRoomAgreement(true);
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(principal instanceof User) {
			User user = (User)principal;
			userService.acceptReadingRoomAgreement(user.getId());
		}
		
		return request.isRequestedSessionIdValid();
	}	
	
	@ResponseBody
	@RequestMapping(value = "/endSession", method = RequestMethod.POST)
	public ResponseEntity<String> endSession(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public Map<String, VhmmlMessage> getMessages(HttpServletRequest request) throws IOException {
		
		Map<String, VhmmlMessage> messages = messageUtil.getGlobalMessages();
		VhmmlSession session = VhmmlSession.getSession(request);
		List<String> removedMessages = session != null ? session.getRemovedMessages() : null;
		
		if(MapUtils.isNotEmpty(messages) && CollectionUtils.isNotEmpty(removedMessages)) {
			for(String messageKey : removedMessages) {
				messages.remove(messageKey);
			}
		}		
		
		return messages;
	}
	
	@RequestMapping(value = "/messages/remove/{messageKey}", method = RequestMethod.POST)
	public  ResponseEntity<String> removeGlobalMessage(@PathVariable String messageKey, HttpServletRequest request) throws IOException {
		ResponseEntity<String> response = new ResponseEntity<String>("Message removed", HttpStatus.OK);
		VhmmlSession session = VhmmlSession.getSession(request);
		session.getRemovedMessages().add(messageKey);		
		
		return response;
	}
}