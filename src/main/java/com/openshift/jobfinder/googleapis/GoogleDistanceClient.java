package com.openshift.jobfinder.googleapis;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;

@Component
public class GoogleDistanceClient {

	// Create our transport.
	private static final HttpTransport transport = new ApacheHttpTransport();

	private static final String DIRECTION_SEARCH_URL = "http://maps.googleapis.com/maps/api/distancematrix/json?";

	public DistanceResponse findDirections(double[] origins,
			double[] destinations) {
		try {
			System.out.println("Finding direction for origin " + origins
					+ " , and destination " + destinations);

			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(DIRECTION_SEARCH_URL));
			// request.url.put("key", API_KEY);
			request.url.put("origins", origins[0] + "," + origins[1]);
			request.url.put("destinations", destinations[0] + ","
					+ destinations[1]);
			request.url.put("sensor", "false");

			System.out.println(request.url.toString());
			String json = request.execute().parseAsString();

			System.out.println(json);
			Gson gson = new Gson();
			DistanceResponse response = gson.fromJson(json,
					DistanceResponse.class);
			return response;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {

		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("JobsNearYou");
				request.headers = headers;
				JsonHttpParser parser = new JsonHttpParser();
				parser.jsonFactory = new JacksonFactory();
				request.addParser(parser);
			}
		});
	}

}
