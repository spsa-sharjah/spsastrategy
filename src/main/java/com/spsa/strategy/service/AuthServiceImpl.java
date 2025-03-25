package com.spsa.strategy.service;

import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.ResrictedGoalRolesRq;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Restrictedgoalsuserlevel;
import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.RestrictedgoalsuserlevelRepository;
import com.spsa.strategy.rest.call.VerifyAuth;

import jakarta.validation.Valid;



@Service
public class AuthServiceImpl implements AuthService {

	@Value("${spring.auth.endpoint.api}") 
	private String authendpointapi;
	@Value("${spring.spsa.auth.api}") 
	private String authapi;

	@Autowired
	MessageService messageService;

	@Autowired
	RestrictedgoalsuserlevelRepository restrictedgoalsuserlevelRepository;
	
	@Override
	public ResponseEntity<?> callAuth(String apikey, String apisecret, String username, String token, String url, String lang) {
		try {

			if (token == null || token.trim().equals("")) 
				return new ResponseEntity<MessageResponse>(new MessageResponse("Token is required", 310), HttpStatus.OK);
			
			VerifyAuth verifyAuth = new VerifyAuth(authendpointapi + authapi, apikey, apisecret, username, token, lang);
			String verifyAuthRes = verifyAuth.callAsPost();
			if (verifyAuthRes == null)
				return new ResponseEntity<MessageResponse>(new MessageResponse("Error while calling verify Auth service", 311), HttpStatus.OK);
	
			JSONObject verifyAuthResponse = new JSONObject(verifyAuthRes);

			if (verifyAuthResponse == null || !verifyAuthResponse.has("user_id")) {

				return new ResponseEntity<String>(verifyAuthResponse.toString(), HttpStatus.OK);
			}

			Users user = new Users(verifyAuthResponse);
			
			//check api authorization
			if (!Utils.isapiauthorized(url, null, user.getAuthorizedapis()))
				return new ResponseEntity<MessageResponse>(new MessageResponse("API unauthorized", 312), HttpStatus.OK);
			
			return new ResponseEntity<Users>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<MessageResponse>(new MessageResponse(e.getMessage(), 3141), HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<?> rolegoalsaccesssave(Locale locale, Users user, @Valid ResrictedGoalRolesRq req) {
		try {
			restrictedgoalsuserlevelRepository.deleteByGoalid(req.getGoalid());
			for (String restrictrole : req.getRoles()) {
				Restrictedgoalsuserlevel restrictedgoalsuserlevel = new Restrictedgoalsuserlevel(req.getGoalid(), restrictrole);
				restrictedgoalsuserlevelRepository.save(restrictedgoalsuserlevel);
			}
			
			return new ResponseEntity<MessageResponse>(new MessageResponse(messageService.getMessage("success_operation", locale)), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<MessageResponse>(new MessageResponse(e.getMessage(), 3142), HttpStatus.OK);
			
		}
	}

	@Override
	public ResponseEntity<?> rolegoalaccesslist(Locale locale, Users user, String goalid) {
		try {
			List<Restrictedgoalsuserlevel> restrictedgoalsuserlevels = restrictedgoalsuserlevelRepository.findByGoalid(goalid);
			
			return new ResponseEntity<List<Restrictedgoalsuserlevel>>(restrictedgoalsuserlevels, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<MessageResponse>(new MessageResponse(e.getMessage(), 3143), HttpStatus.OK);
			
		}
	}
	
}
