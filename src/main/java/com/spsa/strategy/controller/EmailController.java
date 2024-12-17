package com.spsa.strategy.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spsa.strategy.builder.request.EmailDetailsRq;
import com.spsa.strategy.service.EmailService;
import com.spsa.strategy.service.GoogleRecaptchaService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/api")
public class EmailController {

	@Autowired
	EmailService emailService;

	@Autowired
	GoogleRecaptchaService googleRecaptchaService;
	
	@RequestMapping(value = {"/email", "/{version}/email"}, 
			method = RequestMethod.POST, headers = "Accept=application/json") 
	public ResponseEntity<?> sendEmail(
			HttpServletRequest request, 
			@PathVariable(name = "version", required = false) String version,
			@RequestBody EmailDetailsRq rq) throws UnsupportedEncodingException { 

//		ResponseEntity<String> verifycaptcha = googleRecaptchaService.verifyRecaptcha(rq.getCaptchaToken());
//		if (verifycaptcha != null) 
//			return verifycaptcha;
		
		boolean sent = emailService.sendSimpleMail(rq);

		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("success", sent);
		
		JSONObject response = new JSONObject(map);
		
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}
}
