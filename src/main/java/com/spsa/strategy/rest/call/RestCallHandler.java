package com.spsa.strategy.rest.call;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class RestCallHandler { 
	private String url; // api
	protected RestTemplate restTemplate = new RestTemplate();
	protected String body;
	protected HttpHeaders headers;
	protected ResponseEntity<String> result;
	protected String query = null;
	protected ResponseEntity<byte[]> docResult;
	
	
	public RestCallHandler(String url) {
		this.url = url;
	}	
	
	/**
	 * initialize the restTemplate object
	 */
	protected void initRestTemplate() {
		this.restTemplate = new RestTemplate();
	}
	
	/**
	 * implementation of this class is used to construct the HttpHeaders headers object
	 * please note that headers is equal to null if on construction of the current object
	 */
	public abstract void constructHeaders();
	/**
	 * implementation of this class is used to construct the HashMap<Object, Object> body object
	 * please note that body is equal to null if on construction of the current object
	 */
	public abstract void constructBody();
	
	
	public String callAsPost() {
		this.initRestTemplate();
		this.constructHeaders();
		this.constructBody();
		
		HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
		this.result = restTemplate.exchange(this.url, HttpMethod.POST, entity, String.class);
		return result.getBody();
	}
	 
	/**
	 * executes the call built in the class as a get call 
	 * @return the response body from the call as string
	 * @throws ProjectException 
	 */
	public String callAsGet() {
		this.constructHeaders();
		this.initRestTemplate();

		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		this.result = restTemplate.exchange(this.url, HttpMethod.GET, entity, String.class);
		return result.getBody();
	}
	

	public byte[] callAsGetDoc() {
		this.constructHeaders();
		this.initRestTemplate();

		HttpEntity<?> entity = new HttpEntity<Object>(headers);
		this.docResult = restTemplate.exchange(this.url, HttpMethod.GET, entity, byte[].class);
		return this.docResult.getBody();
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
