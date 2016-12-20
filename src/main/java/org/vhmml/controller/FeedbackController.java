package org.vhmml.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vhmml.entity.User;
import org.vhmml.form.CorrectionsForm;
import org.vhmml.service.FeedbackService;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

	private static final Logger LOG = Logger.getLogger(FeedbackController.class);
	
	@Autowired
	private FeedbackService feedbackService;
	
	public FeedbackController() {
		super();
	}

	@RequestMapping(value = "/submitCorrections", method = RequestMethod.POST)
	public ResponseEntity<String> submitCorrections(@ModelAttribute CorrectionsForm correctionsForm, BindingResult result) {
		ResponseEntity<String> response = new ResponseEntity<String>("Your message was sent successfully. Thank you for your feedback!", HttpStatus.OK);
		String contactEmail = correctionsForm.isAllowContact() ? correctionsForm.getContactEmail() : null;
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			if(correctionsForm.isAllowContact() && auth != null && !auth.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {
				contactEmail = ((User)auth.getPrincipal()).getUsername();
			}
			
			feedbackService.sendCorrections(correctionsForm.getType().getDisplayName(), correctionsForm.getCategories(), correctionsForm.getCorrections(), contactEmail, correctionsForm.getHmmlProjectNumber());
		} catch(Exception e) {			
			LOG.error("Unexpected exception attempting to email corrections to system librarian", e);
			response = new ResponseEntity<String>("There was an unexpected error while trying to send your message. If the problem persists please contact the vHMML system administrator.", HttpStatus.INTERNAL_SERVER_ERROR);			
		}		
		
		return response;
	}
}
