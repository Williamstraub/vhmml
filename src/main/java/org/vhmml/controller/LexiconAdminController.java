package org.vhmml.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.LexiconSearchResult;
import org.vhmml.dto.elasticsearch.IndexedType;
import org.vhmml.entity.Contributor;
import org.vhmml.entity.LexiconTerm;
import org.vhmml.form.EditTermForm;
import org.vhmml.service.ElasticSearchService;
import org.vhmml.service.LexiconService;
import org.vhmml.web.VhmmlMessage;
import org.vhmml.web.VhmmlMessage.Severity;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/lexicon/admin")
public class LexiconAdminController extends LexiconBaseController {
	
	private static final Logger LOG = Logger.getLogger(LexiconAdminController.class);
	
	public static final String VIEW_LEXICON_EDIT = "lexicon/edit";
	
	public static final String REQUEST_ATT_LEXICON_TERM = "lexiconTerm";
	
	public static final String LIST_TYPE_RELATED_TERMS = "relatedTerms";
	public static final String LIST_TYPE_CONTRIBUTORS = "contributors";
	
	private static ObjectMapper objectMapper;
	
	@Autowired
	private LexiconService lexiconService;
	
	@Autowired
	private ElasticSearchService elasticSearchService;
	
	static {
		objectMapper = new ObjectMapper();
	}

	@ResponseBody
	@RequestMapping(value = {"/getAutoCompleteList", "/getAutoCompleteList/"}, method = RequestMethod.GET)
	public List<String> getAutoCompleteList(@RequestParam String term, @RequestParam String listType) {
		List<String> autoCompleteOptions = new ArrayList<String>();
		
		if(LIST_TYPE_RELATED_TERMS.equals(listType)) {
			LexiconSearchResult searchResult = lexiconService.termContainsSearch(term);
			autoCompleteOptions = getAutoCompleteOptions(searchResult.getTerms(), "term");
		} else if(LIST_TYPE_CONTRIBUTORS.equals(listType)) {
			autoCompleteOptions = getAutoCompleteOptions(lexiconService.findContributorsByName(term), "name");		
		}		
		
		return autoCompleteOptions;
	}
	
	@RequestMapping("/add")
	public ModelAndView addTerm() throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_LEXICON_EDIT);
		modelAndView.addObject(REQUEST_ATT_LEXICON_TERM, new LexiconTerm());				
		
		return modelAndView;
	}
	
	/**
	 * 
	 * @param id
	 * @param searchText - The search text used to trim results on the Lexicon landing page, used 
	 * 	so we can bring the user back to the Lexicon page in the same state it was when they came to the edit page. 
	 * @param startsWithLetter - The selected "starts with" letter used to trim results on the Lexicon landing page, used 
	 * 	so we can bring the user back to the Lexicon page in the same state it was when they came to the edit page.
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/edit/{id}")
	public ModelAndView editTerm(@PathVariable("id") Long id, @RequestParam(required = false) String searchText, @RequestParam(required = false) String startsWithLetter, @RequestParam(required = false) Integer selectedPage) throws IOException {
		ModelAndView modelAndView = new ModelAndView(VIEW_LEXICON_EDIT);
		
		if(id != null) {
			LexiconTerm term = lexiconService.findById(id);
			modelAndView.addObject(REQUEST_ATT_LEXICON_TERM, term);
		}
						
		modelAndView.addObject(REQUEST_ATT_SEARCH_TEXT, searchText);				
		modelAndView.addObject(REQUEST_ATT_STARTS_WITH_LETTER, startsWithLetter);				
		modelAndView.addObject(REQUEST_ATT_SELECTED_PAGE, selectedPage);				
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute @Valid EditTermForm editTermForm, BindingResult result, @RequestParam(required = false) String searchText, @RequestParam(required = false) String startsWithLetter, @RequestParam(required = false) Integer selectedPage) throws IOException {
		ModelAndView modelAndView = editTerm(editTermForm.getId(), searchText, startsWithLetter, selectedPage);
		
		try {
			LexiconTerm updatedTerm = lexiconService.save(getLexiconTerm(editTermForm));		
			modelAndView = editTerm(updatedTerm.getId(), searchText, startsWithLetter, selectedPage);
			VhmmlMessage message = new VhmmlMessage("Term saved successfully.");
			modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
		} catch(Exception e) {			
			VhmmlMessage message = new VhmmlMessage("An unexpected error occuring while trying to save the term. Please check the server logs for more details.", Severity.ERROR);
			modelAndView.addObject(ControllerConstants.REQUEST_ATT_PAGE_MESSAGES, objectMapper.writeValueAsString(message));
		}		
		
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> removeTerm(@PathVariable("id") Long id) throws IOException {
		ResponseEntity<String> response = new ResponseEntity<String>("Term removed successfully.", HttpStatus.OK);
		
		try {
			lexiconService.remove(id);
		} catch(Exception e) {
			LOG.error("Exception attempting to remove term for id " + id, e);
			response = new ResponseEntity<String>("An unexpected error occured while attempting to remove term!", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
				
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/reindex", method = RequestMethod.POST)
	public ResponseEntity<String> reindex() {
		ResponseEntity<String> response = new ResponseEntity<String>("Re-index completed successfully.", HttpStatus.OK);
		
		try {
			elasticSearchService.reindex(IndexedType.LEXICON_TERM, lexiconService.findAll());
		} catch(Exception e) {
			LOG.error("Unexpected exception while trying to re-index lexicon data.", e);
			response = new ResponseEntity<String>("Re-index failed to complete. Please contact the system administrator if the problem persists.", HttpStatus.INTERNAL_SERVER_ERROR);
		}		
		
		return response;
	}
	
	private LexiconTerm getLexiconTerm(EditTermForm editTermForm) {
		LexiconTerm term = new LexiconTerm();
		BeanUtils.copyProperties(editTermForm, term);
		String relatedTermsString = editTermForm.getRelatedTerms();
		String contributorsString = editTermForm.getContributors();
		
		if(!StringUtils.isEmpty(relatedTermsString)) {
			List<LexiconTerm> relatedTerms = new ArrayList<LexiconTerm>();
			String[] relatedTermsArray = relatedTermsString.split("\\|");
			
			for(String relatedTerm : relatedTermsArray) {
				relatedTerms.add(new LexiconTerm(relatedTerm));
			}
			
			term.setRelatedTerms(relatedTerms);
		}
		
		if(!StringUtils.isEmpty(contributorsString)) {
			List<Contributor> contributors = new ArrayList<Contributor>();
			String[] contributorsArray = contributorsString.split("\\|");
			
			for(String contributorName : contributorsArray) {
				contributors.add(new Contributor(contributorName));
			}
			
			term.setContributors(contributors);
		}
		
		return term;
	}
	
	private List<String> getAutoCompleteOptions(List<?> objects, String textAttributeName) {
		
		List<String> autoCompleteOptions = new ArrayList<String>();		
		
		if(!CollectionUtils.isEmpty(objects)) {
			for(Object object : objects) {
				try {
					autoCompleteOptions.add(org.apache.commons.beanutils.BeanUtils.getProperty(object, textAttributeName).toString());
				} catch(Exception e) {
					throw new RuntimeException("Unable to get autocomplete list for object of type " + object.getClass() + " using text attribute " + textAttributeName, e);
				}
			}
		}		
		
		return autoCompleteOptions;
	}
}
