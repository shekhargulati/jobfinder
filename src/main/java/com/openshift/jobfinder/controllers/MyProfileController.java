package com.openshift.jobfinder.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openshift.jobfinder.domain.Account;
import com.openshift.jobfinder.jdbc.repository.AccountRepository;

@Controller
public class MyProfileController {

	@Inject
	private ConnectionRepository connectionRepository;
	@Inject
	private AccountRepository accountRepository;

	@RequestMapping(value = "/myprofile", method = RequestMethod.GET)
	public String home(Principal currentUser, Model model) {
		Connection<LinkedIn> connection = connectionRepository
				.findPrimaryConnection(LinkedIn.class);
		
		String username = currentUser.getName();
		Account account = accountRepository.findAccountByUsername(username);
		if (connection == null) {
			model.addAttribute("profile",new UserProfileVo(account));
			return "profile/myprofile";
		}
		LinkedInProfile linkedinProfile = connection.getApi().profileOperations()
				.getUserProfile();
		model.addAttribute("profile", new UserProfileVo(account, linkedinProfile));
		model.addAttribute("isConnected",true);
		return "profile/myprofile";
	}

	
}