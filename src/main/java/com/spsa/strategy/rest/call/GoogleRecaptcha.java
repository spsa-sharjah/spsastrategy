package com.spsa.strategy.rest.call;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class GoogleRecaptcha extends RestCallHandler{ 

	private String siteKey;
	private String token;
	
	public GoogleRecaptcha(String api, String token, String siteKey) {
		super(api);
		this.siteKey = siteKey;
		this.token = token;
	} 
	 
	@Override
	public void constructHeaders() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Override
	public void constructBody() {

		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> mapchild = new HashMap<String, String>();
		mapchild.put("expectedAction", "USER_ACTION");
		mapchild.put("siteKey", siteKey);
		mapchild.put("token", token);
		map.put("event", mapchild);
		
		JSONObject request = new JSONObject(map);

		this.body = request.toString();
		
	}

}
