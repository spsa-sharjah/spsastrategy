package com.spsa.strategy.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.rest.call.VerifyAuth;



@Service
public class AuthServiceImpl implements AuthService {

	@Value("${spring.spsa.auth.api}") 
	private String api;
	
	@Override
	public ResponseEntity<?> callAuth(String apikey, String apisecret, String username, String token, String url, String lang) {
		try {

			if (token == null || token.trim().equals("")) {

				MessageResponse messageResponse = new MessageResponse("token is required", 310);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			VerifyAuth verifyAuth = new VerifyAuth(api, apikey, apisecret, username, token, lang);
			String verifyAuthRes = verifyAuth.callAsPost();
			if (verifyAuthRes == null) {

				MessageResponse messageResponse = new MessageResponse("Error while calling verify Auth service", 311);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
	
			JSONObject verifyAuthResponse = new JSONObject(verifyAuthRes);

			if (verifyAuthResponse == null || !verifyAuthResponse.has("user_id")) {

				return new ResponseEntity<String>(verifyAuthResponse.toString(), HttpStatus.OK);
			}

			Users user = new Users(verifyAuthResponse);
			if (!Utils.isapiauthorized(url, user.getAuthorizedapis())) {

				MessageResponse messageResponse = new MessageResponse("Not authorized to use the " + url + " API", 401);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			return new ResponseEntity<Users>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			MessageResponse messageResponse = new MessageResponse(e.getMessage(), 314);
			return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			
		}
	}
	
}
