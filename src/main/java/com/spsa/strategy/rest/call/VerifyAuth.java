package com.spsa.strategy.rest.call;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class VerifyAuth extends RestCallHandler{ 

	private String apikey;
	private String apisecret;
	private String username;
	private String token;
	private String lang;
	
	public VerifyAuth(String api, String apikey, String apisecret, String username, String token, String lang) {
		super(api);
		this.apikey = apikey;
		this.apisecret = apisecret;
		this.username = username;
		this.token = token;
		this.lang = lang;
	} 
	 
	@Override
	public void constructHeaders() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.headers.set("token", token); 
		this.headers.set("apikey", apikey); 
		this.headers.set("apisecret", apisecret); 
		this.headers.set("username", username); 
		this.headers.set("Accept-Language", lang); 
	}

	@Override
	public void constructBody() {
	}

}
