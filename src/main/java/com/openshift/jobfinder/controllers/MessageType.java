package com.openshift.jobfinder.controllers;

public enum MessageType {
	
	/**
	 * The message is informative in nature, like a note or notice.
	 */
	INFO, 

	/**
	 * The message indicates that an action initiated by the user was performed successfully.
	 */
	SUCCESS, 
	
	/**
	 * The message warns the user something is not quite right.
	 * Corrective action is generally recommended but not required.
	 */
	WARNING, 
	
	/**
	 * The message reports an error condition that needs to be addressed by the user.
	 */
	ERROR;
	
	private final String cssClass;
	
	private MessageType() {
		cssClass = name().toLowerCase(); 
	}
	
	/**
	 * The css class for styling messages of this type.
	 */
	public String getCssClass() {
		return cssClass;
	}
}