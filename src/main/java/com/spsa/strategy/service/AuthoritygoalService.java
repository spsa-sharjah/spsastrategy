package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.AuthoritygoalSaveRq;

import jakarta.validation.Valid;

public interface AuthoritygoalService {
	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String username, String strategylevelid);

	ResponseEntity<?> goalsave(Locale locale, @Valid AuthoritygoalSaveRq req, String username, String strategylevelid);

	ResponseEntity<?> goalremove(Locale locale, String goalid, String username, String strategylevelid);

}
