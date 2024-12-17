package com.spsa.strategy.controller;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spsa.strategy.builder.request.SMSDetailsRq;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.service.GoogleRecaptchaService;
import com.spsa.strategy.service.SMSService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(path = "/api")
public class SMSController {

	@Autowired
	SMSService smsService;

	@Autowired
	GoogleRecaptchaService googleRecaptchaService;
	
	@RequestMapping(value = {"/sms", "/{version}/sms"}, 
			method = RequestMethod.POST, headers = "Accept=application/json") 
	public ResponseEntity<?> sendSMS(
			@RequestHeader(name = "Accept-Language", required = false) Locale locale,
			HttpServletRequest request, 
			@PathVariable(name = "version", required = false) String version,
			@RequestBody SMSDetailsRq rq) throws UnsupportedEncodingException { 

//		ResponseEntity<?> verifycaptcha = googleRecaptchaService.verifyRecaptcha(rq.getCaptchaToken());
//		if (verifycaptcha != null) 
//			return verifycaptcha;
		
		
		if (rq.getMsgBody().equalsIgnoreCase(Constants.OTP_SMS_KEY)) {

			
			rq.setMsgBody(Constants.returnOTPSMSMessage());
		}
			
		ResponseEntity<?> sendsms = smsService.sendSMS(locale, rq);
		if (sendsms != null)
			return sendsms;
		
		return new ResponseEntity<MessageResponse>(new MessageResponse("SMS successfully sent"), HttpStatus.OK);
		
	}
}
