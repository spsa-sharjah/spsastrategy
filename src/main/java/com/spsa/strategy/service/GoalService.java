package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.EndorseGoalsRq;
import com.spsa.strategy.builder.request.YearlySettingsRq;
import com.spsa.strategy.model.Users;

import jakarta.validation.Valid;

public interface GoalService {

	ResponseEntity<?> list(Locale locale, Users user, String menuauthid);

	ResponseEntity<?> authoritygoaltree(Users user, Locale locale, String year, String team, boolean showdepartment,
			boolean showsection, boolean showevidence);

	ResponseEntity<?> authoritygoalendorse(Users user, Locale locale, @Valid EndorseGoalsRq req);

	ResponseEntity<?> yearlysettings(Users user, Locale locale, String year);

	ResponseEntity<?> yearlysettingsave(Users user, Locale locale, @Valid YearlySettingsRq req);

	ResponseEntity<?> yearlysettingendorsementready(Users user, Locale locale, String year);

}
