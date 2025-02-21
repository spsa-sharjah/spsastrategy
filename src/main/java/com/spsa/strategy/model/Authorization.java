package com.spsa.strategy.model;

import org.json.JSONObject;

public class Authorization {

	private String userRole;

	private String menuauthid;

	private String api;

	private boolean enable;
	
	public Authorization(JSONObject verifyAuthResponse) {
		this.userRole = verifyAuthResponse.has("userRole") && !verifyAuthResponse.get("userRole").equals(null) ? verifyAuthResponse.getString("userRole") : null;
		this.api = verifyAuthResponse.has("api") && !verifyAuthResponse.get("api").equals(null) ? verifyAuthResponse.getString("api") : null;
		this.enable = verifyAuthResponse.has("enable") && !verifyAuthResponse.get("enable").equals(null) ? verifyAuthResponse.getBoolean("enable") : false;
		this.menuauthid = verifyAuthResponse.has("menuauthid") && !verifyAuthResponse.get("menuauthid").equals(null) ? verifyAuthResponse.getString("menuauthid") : null;
	}

	public Authorization() {
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getMenuauthid() {
		return menuauthid;
	}

	public void setMenuauthid(String menuauthid) {
		this.menuauthid = menuauthid;
	}
}
