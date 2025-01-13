package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;
import com.spsa.strategy.model.Users;

import jakarta.validation.Valid;

public interface AuthoritygoalService {
	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String username, Users user, Boolean all);

	ResponseEntity<?> goalsave(Locale locale, @Valid AuthoritygoalSaveRq req, String username, Users user);

	ResponseEntity<?> goalremove(Locale locale, String goalid, String username, Users user);

	ResponseEntity<?> details(Locale locale, String goalid, String username, Users user);

	ResponseEntity<?> authoritygoalweight(Locale locale, String username, Users user, String year, String goalid);

}
