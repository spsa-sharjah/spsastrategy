package com.spsa.strategy.service;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.model.Settings;

public interface SettingsService {

	Settings returndefaultSettings();
	
	String getspsalogskey();
	
	ResponseEntity<?> yearslist(int fromyear);

	String returnServerkey();
	String returnServerpass();
}
