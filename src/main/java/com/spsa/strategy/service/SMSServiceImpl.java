package com.spsa.strategy.service;

import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.SMSDetailsRq;
import com.spsa.strategy.builder.response.MessageResponse;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.rest.call.SendSMS;

@Service
public class SMSServiceImpl implements SMSService {

	@Value("${spring.sms.auth.api}") 
	private String authapi;

	@Value("${spring.sms.auth.username}") 
	private String authusername;

	@Value("${spring.sms.auth.password}") 
	private String authpassword;

	@Value("${spring.sms.api}") 
	private String smsapi;

	@Value("${spring.sms.applicationId}") 
	private String smsapplicationid;

	@Value("${spring.sms.password}") 
	private String smspassword;

    @Autowired
    private MessageService messageService;
	
	public ResponseEntity<?> sendSMS(Locale locale, SMSDetailsRq details){

		try {
			SendSMS sendSMS = new SendSMS(authapi, authusername, authpassword, smsapi, smsapplicationid, smspassword, details.getRecipient(), details.getMsgBody());
			String res = sendSMS.callAsPost();
			if (res == null) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("error_calling_send_sms", locale), 301);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
	
			JSONObject resjson = new JSONObject(res);

			if (resjson == null || !resjson.has("response")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("error_parsing_send_sms_resp", locale), 302);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			
			JSONObject resdata = resjson.getJSONObject("response");
			
			if (resdata == null || !resdata.has("header")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("error_parsing_send_sms_resp_header", locale), 303);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
		
			JSONObject resheader = resdata.getJSONObject("header");
			
			if (resheader == null || !resheader.has("status")) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("error_parsing_send_sms_resp_header_status", locale), 304);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}

			String resstatus = resheader.getString("status");
			
			if (!resstatus.equals(Constants.SUCCESS_KEY)) {

				MessageResponse messageResponse = new MessageResponse(messageService.getMessage("send_sms_failed", locale), 305);
				return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
			}
			
		} catch (Exception e) {
			e.printStackTrace();

			MessageResponse messageResponse = new MessageResponse(messageService.getMessage("error_sending_sms", locale), 306);
			return new ResponseEntity<MessageResponse>(messageResponse, HttpStatus.OK);
		}
		
		return null;
	}

}
