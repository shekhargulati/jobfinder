package com.openshift.jobfinder.googleapis;

import com.google.api.client.util.Key;

public class Place {

	@Key
	public String id;
	
	public String formatted_address;
	
	public Geometry geometry;
	@Key
	public String name;
	
	@Key
	public String reference;

	@Override
	public String toString() {
		return "Place [formatted_address=" + formatted_address + ", geometry="
				+ geometry + ", name=" + name + ", reference=" + reference
				+ "]";
	}

	
}
