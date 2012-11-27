package com.openshift.jobfinder.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Document(collection = "jobs")
public class Job {

	@Id
	private String id;

	private CompanyInformation company;

	private String jobTitle;

	private double[] location;

	private String[] skills;

	private String formattedAddress;
	
	private String[] appliedBy;

	public Job(String id,String jobTitle,String[] skills, double[] location){
		this.id = id;
		this.jobTitle = jobTitle;
		this.skills = skills;
		this.location = location;
	}
	
	public Job() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CompanyInformation getCompany() {
		return company;
	}

	public void setCompany(CompanyInformation company) {
		this.company = company;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public double[] getLocation() {
		return location;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setAppliedBy(String[] appliedBy) {
		this.appliedBy = appliedBy;
	}
	
	public String[] getAppliedBy() {
		return appliedBy;
	}
	
	
	public String toJson() {
		return new JSONSerializer().include("location").include("company")
				.include("skills").include("appliedBy").exclude("*.class").serialize(this);
	}

	public static Job fromJson(String json) {
		return new JSONDeserializer<Job>().use(null, Job.class).deserialize(
				json);
	}

	public static String toJsonArray(Collection<Job> collection) {
		return new JSONSerializer().include("location").include("company")
				.include("skills").include("skills").exclude("*.class").serialize(collection);
	}

	public static Collection<Job> fromJsonArray(String json) {
		return new JSONDeserializer<List<Job>>().use(null, ArrayList.class)
				.use("location", Job.class).deserialize(json);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime
				* result
				+ ((formattedAddress == null) ? 0 : formattedAddress.hashCode());
		result = prime * result
				+ ((jobTitle == null) ? 0 : jobTitle.hashCode());
		result = prime * result + Arrays.hashCode(location);
		result = prime * result + Arrays.hashCode(skills);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (formattedAddress == null) {
			if (other.formattedAddress != null)
				return false;
		} else if (!formattedAddress.equals(other.formattedAddress))
			return false;
		if (jobTitle == null) {
			if (other.jobTitle != null)
				return false;
		} else if (!jobTitle.equals(other.jobTitle))
			return false;
		if (!Arrays.equals(location, other.location))
			return false;
		if (!Arrays.equals(skills, other.skills))
			return false;
		return true;
	}
	
	

}
