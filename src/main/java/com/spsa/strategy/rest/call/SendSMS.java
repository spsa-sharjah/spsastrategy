package com.spsa.strategy.rest.call;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.spsa.strategy.config.Constants;

public class SendSMS extends RestCallHandler{ 

	private String authapi;
	private String authusername;
	private String authpassword;
	
	private String applicationId;
	private String password;
	private String mobileNumber;
	private String messageText;
	public boolean validRequest;
	
	public SendSMS(String authapi, String authusername, String authpassword,
			String api, String applicationId, String password, String mobileNumber, String messageText) {
		super(api);
		this.authapi = authapi;
		this.authusername = authusername;
		this.authpassword = authpassword;
		this.applicationId = applicationId;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.messageText = messageText;
		this.validRequest = false;

	} 
	 
	@Override
	public void constructHeaders() {
		this.headers = new HttpHeaders();
		this.headers.setContentType(MediaType.APPLICATION_JSON);
		
		SMSAuthentication smsauthentication = new SMSAuthentication(authapi, authusername, authpassword);

		String smsauthstr = smsauthentication.callAsPost();
		if (smsauthstr != null) {
			JSONObject smsauthObj = new JSONObject(smsauthstr);
			if (smsauthObj != null && 
					smsauthObj.has(Constants.ACCESS_TOKEN_KEY)) {
				this.validRequest = true;
				String bearertoken = Constants.TOKEN_TYPE_BEARER + smsauthObj.getString(Constants.ACCESS_TOKEN_KEY);
				this.headers.set("Authorization", bearertoken); 
			}
		}
	}

	@Override
	public void constructBody() { 
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();

		HashMap<String, String> mapheaderchild = new HashMap<String, String>();
		mapheaderchild.put("applicationId", applicationId);
		mapheaderchild.put("password", password);
		map.put("header", mapheaderchild);

		HashMap<String, String> mapbodychild = new HashMap<String, String>();
		mapbodychild.put("mobileNumber", mobileNumber);
		mapbodychild.put("messageText", messageText);
		map.put("body", mapbodychild);
		
		JSONObject request = new JSONObject(map);
		this.body = request.toString();
	}

}
