package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.SMSDetailsRq;

public interface SMSService {

	ResponseEntity<?> sendSMS(Locale locale, SMSDetailsRq details);

}
