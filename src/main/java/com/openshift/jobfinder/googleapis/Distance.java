package com.openshift.jobfinder.googleapis;

public class Distance {
	public String text;
	public String value;
	
	@Override
	public String toString() {
		return "Distance [text=" + text + ", value=" + value + "]";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
