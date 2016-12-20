package org.vhmml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/scriptorium")
public class ScriptoriumController {
	
	public static final String VIEW_SCRIPTORIUM_HOME = "scriptorium/home";
	public static final String VIEW_SCRIPTORIUM_HELP = "scriptorium/help";
	
	@RequestMapping({"", "/"})
	public ModelAndView home() {
		return new ModelAndView(VIEW_SCRIPTORIUM_HOME);
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public ModelAndView viewHelp() {
		return new ModelAndView(VIEW_SCRIPTORIUM_HELP);
	}
}
