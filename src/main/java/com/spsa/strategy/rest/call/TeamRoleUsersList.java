package com.spsa.strategy.rest.call;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


public class TeamRoleUsersList extends RestCallHandler{ 

	private String apikey;
	private String apisecret;
	
	private String serverkey;
	private String serverpass;
	
	private String lang;
	private String team;
	private String role;
	
	public TeamRoleUsersList(String api, String apikey, String apisecret, String serverkey, String serverpass, String lang, String team, String role) {
		super(api);
		this.apikey = apikey;
		this.apisecret = apisecret;
		this.serverkey = serverkey;
		this.serverpass = serverpass;
		this.team = team;
		this.role = role;
		this.lang = lang;
	} 
	 
	@Override
	public void constructHeaders() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		this.headers.set("apikey", apikey); 
		this.headers.set("apisecret", apisecret); 
		this.headers.set("serverkey", serverkey); 
		this.headers.set("serverpass", serverpass); 
		if (team != null) this.headers.set("team", team); 
		if (role != null) this.headers.set("role", role); 
		this.headers.set("Accept-Language", lang); 
	}

	@Override
	public void constructBody() {
	}

}
