package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.DepartmentgoalSaveRq;

import jakarta.validation.Valid;

public interface DepartmentgoalService {
	ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, String goalid, String strategylevelid);

	ResponseEntity<?> goalsave(Locale locale, @Valid DepartmentgoalSaveRq req, String username, String strategylevelid);

}
