package com.openshift.jobfinder.googleapis;

import java.util.List;

import com.google.api.client.util.Key;

public class Places {
	
	@Key
	public List<Place> results;
	
	@Key
	public String status;

}
