package com.openshift.jobfinder.controllers;

import org.springframework.social.linkedin.api.LinkedInProfile;

import com.openshift.jobfinder.domain.Account;

public class UserProfileVo {

	private Account account;
	private LinkedInProfile linkedInProfile;
	
	public UserProfileVo(Account account, LinkedInProfile linkedInProfile) {
		this.account = account;
		this.linkedInProfile =linkedInProfile;
	}
	
	public UserProfileVo(Account account){
		this.account = account;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public LinkedInProfile getLinkedInProfile() {
		return linkedInProfile;
	}
}
