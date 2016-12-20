package org.vhmml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.vhmml.entity.Role;
import org.vhmml.entity.User;
import org.vhmml.exception.UserExistsException;
import org.vhmml.form.RegistrationForm;
import org.vhmml.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/registration")
public class RegistrationController extends BaseValidationController {
	
	public static final String VIEW_HOME = "static/home";	
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private UserControllerHelper userControllerHelper;
	
	@RequestMapping(value = {"/{pageName}", "/{pageName}/"}, method = RequestMethod.GET)
	public ModelAndView getPage(@PathVariable("pageName") String pageName) {
		return new ModelAndView("registration/" + pageName);		
	}
	
	@RequestMapping(value = {"", "/", "/register"}, method = RequestMethod.GET)
	public ModelAndView viewRegistration() {		
		return userControllerHelper.getAccountSettingsView(SaveAction.REGISTER, null);
	}
	
	@RequestMapping(value = {"", "/", "/register"}, method = RequestMethod.POST)
	public ModelAndView register(@ModelAttribute @Valid RegistrationForm registrationForm, BindingResult result) throws IOException {
		ModelAndView modelAndView = new ModelAndView(UserController.VIEW_ACCOUNT_SETTINGS);		
		boolean errors = result.hasErrors();
		
		if (!errors) {
			User user = new User();
			BeanUtils.copyProperties(registrationForm, user);
			// user doesn't get any role until they are approved by an admin
			List<Role.Name> roles = new ArrayList<Role.Name>();
			
			try {				
				user = userService.createUser(user, roles);
				Map<String, String> popupMessage = new HashMap<String, String>();
				popupMessage.put("title", "Registration Complete!");
				popupMessage.put("message", "Thank you for registering!<br/><br/>Because of HMML&apos;s agreements with the owning libraries, all applications for access to Reading Room must be reviewed by a HMML librarian. We will do this as quickly as possible, and you should receive a response within one business day (weekends and public holidays are excluded).");
				popupMessage.put("closeButtonLabel", "OK");
				modelAndView = new ModelAndView(VIEW_HOME);
				modelAndView.addObject(UserControllerHelper.REQUEST_ATT_USER, user);	
				modelAndView.addObject(ControllerConstants.REQUEST_ATT_POPUP_MESSAGE, objectMapper.writeValueAsString(popupMessage));
			} catch(UserExistsException e) {
				result.rejectValue("username", null, "This email is already used. Are you re-registering? If so, please use a different email address");
				errors = true;				
			}			
		} 
		
		if(errors) {
			modelAndView.addObject(UserControllerHelper.REQUEST_ATT_SAVE_ACCOUNT_SETTINGS_ACTION, "register");
			modelAndView.addObject(UserControllerHelper.REQUEST_ATT_REGISTRATION_FORM, registrationForm);
			addFieldErrorsToModel(modelAndView, result);
		}
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/validationRules", method = RequestMethod.GET)
	public Map<String, Map<String, Map<String, Object>>> getRegistrationValidationRules() {		
		return super.getValidationRules(RegistrationForm.class);
	}
}
