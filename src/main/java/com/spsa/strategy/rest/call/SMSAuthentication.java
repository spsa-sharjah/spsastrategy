package com.spsa.strategy.rest.call;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class SMSAuthentication extends RestCallHandler{ 
	
	private String username;
	private String password;
	
	public SMSAuthentication(String url, String username, String password) {
		super(url);
		this.username = username;
		this.password = password;

	} 
	 
	@Override
	public void constructHeaders() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Override
	public void constructBody() { 
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userName", username);
		map.put("password", password);
		
		JSONObject request = new JSONObject(map);
		this.body = request.toString();
	}

}
