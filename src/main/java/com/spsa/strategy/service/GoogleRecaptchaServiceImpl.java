package com.spsa.strategy.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.rest.call.GoogleRecaptcha;

import java.util.Locale;

@Service
public class GoogleRecaptchaServiceImpl implements GoogleRecaptchaService {

	@Value("${spring.google.recaptcha.api}") 
	private String api;

	@Value("${spring.google.recaptcha.siteKey}") 
	private String sitekey;

    @Autowired
    private MessageService messageService;

	@Override
	public ResponseEntity<?> verifyRecaptcha(Locale locale, String token) {

		try {

			if (token == null || token.trim().equals("")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("recaptcha_required", locale), 410);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			
			GoogleRecaptcha googleRecaptcha = new GoogleRecaptcha(api, token, sitekey);
			String recaptchaRes = googleRecaptcha.callAsPost();
			if (recaptchaRes == null) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("recaptcha_request_error", locale), 411);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
	
			JSONObject recaptcharesponse = new JSONObject(recaptchaRes);

			if (recaptcharesponse == null || !recaptcharesponse.has("tokenProperties")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("recaptcha_parsing_response_error", locale), 412);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			
			JSONObject tokenProperties = recaptcharesponse.getJSONObject("tokenProperties");
			
			if (tokenProperties == null || !tokenProperties.has("valid") || !tokenProperties.getBoolean("valid")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("recaptcha_verifying_validity_error", locale), 413);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
				
		} catch (Exception e) {
			e.printStackTrace();

			MessageResponse messageResponse = new MessageResponse(messageService.getMessage("recaptcha_verifying_error", locale), 414);
			return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			
		}
		return null;
	}

}
