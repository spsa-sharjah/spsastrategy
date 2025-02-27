package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.DepartmentgoalSaveRq;
import com.spsa.strategy.model.Users;

import jakarta.validation.Valid;

public interface DepartmentgoalService {
	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, Users user, Boolean all);

	ResponseEntity<?> goalsave(Locale locale, @Valid DepartmentgoalSaveRq req, String username, Users user);

	ResponseEntity<?> goalremove(Locale locale, String goalid, String username, Users user);

	ResponseEntity<?> details(Locale locale, String goalid, String username, Users user);

	ResponseEntity<?> departmentgoalweight(Locale locale, String username, Users user, String authgoalid,
			String depgoalid);

	String deletebyauthgoalid(Locale locale, Users user, String authgoalid);

}
