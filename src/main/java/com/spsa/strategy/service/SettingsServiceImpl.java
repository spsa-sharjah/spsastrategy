package com.spsa.strategy.service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.model.Settings;
import com.spsa.strategy.repository.SettingsRepository;

@Service
public class SettingsServiceImpl implements SettingsService {

	@Value("${spring.admin.key}") 
	private String adminkey;

	@Value("${spring.spsalogs.api.key}") 
	private String spsalogsapikey;
	
	@Autowired
	private SettingsRepository settingsRepository;

	@Override
	public Settings returndefaultSettings() {
		List<Settings> settings = settingsRepository.findByIsdefault(true);
		
		if (settings == null || settings.size() == 0 || settings.get(0) == null) {
			Settings builtinsettings = new Settings(adminkey, true);
			return builtinsettings;
		}

		return settings.get(0);
	}

	@Override
	public String getspsalogskey() {
		return spsalogsapikey;
	}
	
	@Override
	public ResponseEntity<?> yearslist(int fromyear) {
        int currentYear = Year.now().getValue();
        List<String> years = new ArrayList<String>();
        for (int i = fromyear; i <= currentYear; i++) {
            years.add(i+"");
        }
		return ResponseEntity.ok(years);
    }
}
