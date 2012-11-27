package com.openshift.jobfinder.controllers;

import org.springframework.social.connect.UserProfile;

public class SignupForm {

	private String username;

	private String password;

	private String firstName;

	private String lastName;
	
	private String address;

	private String skills;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}

	public static SignupForm fromProviderUser(UserProfile providerUser) {
		SignupForm form = new SignupForm();
		form.setFirstName(providerUser.getFirstName());
		form.setLastName(providerUser.getLastName());
		return form;
	}

	public String getSkills() {
		return skills;
	}
	
	public void setSkills(String skills) {
		this.skills = skills;
	}
}
