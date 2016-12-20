package org.vhmml.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.vhmml.dto.LexiconSearchResult;
import org.vhmml.service.LexiconService;

@Controller
@RequestMapping("/lexicon")
public class LexiconController extends LexiconBaseController {
	
	public static final String VIEW_LEXICON_HOME = "lexicon/home";
	public static final String VIEW_LEXICON_DEFINITION = "lexicon/definition";
	public static final String VIEW_LEXICON_HELP = "lexicon/help";
	
	public static final String ATT_LEXICON_TERM = "term";
	
	@Autowired
	private LexiconService lexiconService;
	
	@Autowired
	private PagingUtil pagingUtil;
	
	@RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(required = false) String searchText, @RequestParam(required = false) String startsWithLetter, @RequestParam(required = false) Integer selectedPage) {
		ModelAndView modelAndView = new ModelAndView(VIEW_LEXICON_HOME);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"title"});
		modelAndView.addObject(REQUEST_ATT_SEARCH_TEXT, searchText);
		modelAndView.addObject(REQUEST_ATT_STARTS_WITH_LETTER, startsWithLetter);
		modelAndView.addObject(REQUEST_ATT_SELECTED_PAGE, selectedPage);
		modelAndView.addObject(ControllerConstants.ATT_PAGE_SIZE, defaultPageable.getPageSize());
		modelAndView.addObject(ControllerConstants.ATT_SORT_BY, pagingUtil.getSortString(defaultPageable));

		return modelAndView;
	}
	
	@RequestMapping(value = {"/definition/{id}", "/definition/{id}/"}, method = RequestMethod.GET)
	public ModelAndView viewDefinition(@PathVariable("id") Long id, @RequestParam(required = false) String searchText, @RequestParam(required = false) String startsWithLetter, @RequestParam(required = false) Integer selectedPage) {
		ModelAndView modelAndView = new ModelAndView(VIEW_LEXICON_DEFINITION);
		Pageable defaultPageable = pagingUtil.getDefaultPageable(new String[]{"title"});
		modelAndView.addObject(REQUEST_ATT_SEARCH_TEXT, searchText);
		modelAndView.addObject(REQUEST_ATT_STARTS_WITH_LETTER, startsWithLetter);
		modelAndView.addObject(REQUEST_ATT_SELECTED_PAGE, selectedPage);
		modelAndView.addObject(ControllerConstants.ATT_PAGE_SIZE, defaultPageable.getPageSize());
		modelAndView.addObject(ATT_LEXICON_TERM, lexiconService.findById(id));
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {		
		return new ModelAndView(VIEW_LEXICON_HELP);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/search/{searchText}", "/search/{searchText}/"}, method = RequestMethod.GET)
	public LexiconSearchResult search(@PathVariable("searchText") String searchText, Pageable pageable) {	
		return lexiconService.search(searchText, pageable);
	}
	
	@ResponseBody
	@RequestMapping(value = {"/startsWith/{searchTerm}", "/startsWith/{searchTerm}/"}, method = RequestMethod.GET)
	public LexiconSearchResult startsWithSearch(@PathVariable("searchTerm") String searchTerm, Pageable pageable) {
		return lexiconService.startsWithSearch(searchTerm, pageable);		
	}	
}
