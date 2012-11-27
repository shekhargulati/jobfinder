package com.openshift.jobfinder.controllers;

public class SearchForm {

	private String skills;
	private String location;
	
	private double latitude;
	private double longitude;
	
	private double withinDistance;

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getWithinDistance() {
		return withinDistance;
	}

	public void setWithinDistance(double withinDistance) {
		this.withinDistance = withinDistance;
	}
	
	
}
