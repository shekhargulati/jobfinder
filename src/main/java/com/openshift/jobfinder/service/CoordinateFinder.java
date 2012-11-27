package com.openshift.jobfinder.service;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class CoordinateFinder {

	/**
	 * Returns an array of which first is latitude and second is longitude
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public double[] find(String location) throws Exception {
		location = location.trim().replace(" ", "%20");
		System.out.println(location);
		Document document = Jsoup.connect(
				"http://www.findlatitudeandlongitude.com/?loc=" + location)
				.get();

		String text = document.text();

		if (StringUtils.isBlank(text)) {
			return new double[] {};
		}

		int indexOf = StringUtils.indexOf(text,
				"Selected Location (Approximate)");
		if (indexOf == -1) {
			return new double[] {};
		}
		String str1 = StringUtils.substring(text, 0, indexOf);
		int lastIndexOf = str1.lastIndexOf("Latitude");
		String str2 = StringUtils.substring(str1, lastIndexOf);
		String[] split = str2.split("\\s");
		double[] coordinates = new double[2];
		int i = 0;
		for (String string : split) {
			String a = string.substring(0, string.length() - 1).split(":")[1];
			coordinates[i] = Double.parseDouble(a);
			i++;
		}
		return coordinates;
	}

}
