package org.vhmml.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.controller.validation.JQueryValidationRuleBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseValidationController {   
	
	private static final Logger LOG = Logger.getLogger(BaseValidationController.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	protected Map<String, Map<String, Map<String, Object>>> getValidationRules(Class<?> formBeanClass) {
		return JQueryValidationRuleBuilder.buildJQueryValidationRules(formBeanClass);		
	}
	
	protected void addFieldErrorsToModel(ModelAndView modelAndView, BindingResult bindResult) {		
		if(modelAndView != null && bindResult.hasErrors()) {			
			modelAndView.addObject(ControllerConstants.REQUEST_ATT_FIELD_ERROR_MESSAGES, getFieldErrorsJson(bindResult));			
		}	
	}
	
	protected String getFieldErrorsJson(BindingResult bindResult) {
		String fieldErrorsJson = null;		
		
		if(bindResult.hasErrors()) {			
			List<FieldError> fieldValidationErrors = new ArrayList<>();
			
			for(ObjectError error : bindResult.getAllErrors()) {
				// field, defaultMessage, objectName
				if(FieldError.class.isInstance(error)) {
					FieldError fieldError = (FieldError)error;
					fieldValidationErrors.add(fieldError);
				}				
			}
						
			try {
				fieldErrorsJson = objectMapper.writeValueAsString(fieldValidationErrors);
			} catch (JsonProcessingException e) {
				LOG.error("JsonProcessingException while attempting to serialize field error list", e);
			}					
		}	
		
		return fieldErrorsJson;
	}
}
