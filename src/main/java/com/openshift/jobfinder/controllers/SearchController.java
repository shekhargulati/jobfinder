package com.openshift.jobfinder.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SearchController {
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public SearchForm searchForm(){
		return new SearchForm();
	}

}
