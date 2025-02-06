package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.model.Users;

public interface GoalStatusService {

	ResponseEntity<?> list(Locale locale, Users user);

}
