package org.vhmml.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/school")
public class SchoolController {
	
	public static final String VIEW_HOME = "school/home";
	public static final String VIEW_HELP = "school/help";
	
	public static final String ATT_LESSON_PATH = "lessonPath";
	public static final String ATT_SECTION_PATH = "sectionPath";
	public static final String ATT_SELECTED_SECTION_URL = "selectedSectionUrl";
	
	@RequestMapping({"", "/"})
	public ModelAndView home() {
		return new ModelAndView(VIEW_HOME);
	}
	
	@RequestMapping(value = "help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {
		return new ModelAndView(VIEW_HELP);
	}
	
	@RequestMapping("/lesson/{lessonPath}/{sectionPath}")
	public ModelAndView viewLesson(@PathVariable(ATT_LESSON_PATH) String lessonPath, @PathVariable(ATT_SECTION_PATH) String sectionPath, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("lesson/" + lessonPath + "/" + sectionPath);
		modelAndView.addObject(ATT_LESSON_PATH, lessonPath);
		modelAndView.addObject(ATT_SELECTED_SECTION_URL, request.getServletPath());
		
		return modelAndView;
	}
}
