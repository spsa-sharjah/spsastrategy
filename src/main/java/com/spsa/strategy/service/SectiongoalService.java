package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.SectiongoalSaveRq;

import jakarta.validation.Valid;

public interface SectiongoalService {
	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, String strategylevelid);

	ResponseEntity<?> goalsave(Locale locale, @Valid SectiongoalSaveRq req, String username, String strategylevelid);

	ResponseEntity<?> goalremove(Locale locale, String goalid, String username, String strategylevelid);

	ResponseEntity<?> details(Locale locale, String goalid, String username, String strategylevelid);

}