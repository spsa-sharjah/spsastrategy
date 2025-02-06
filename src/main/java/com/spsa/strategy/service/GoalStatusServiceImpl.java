package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spsa.strategy.model.Users;
import com.spsa.strategy.repository.GoalStatusRepository;

@Service
public class GoalStatusServiceImpl implements GoalStatusService {
	
	@Autowired
	private GoalStatusRepository goalStatusRepository;

	@Override
	public ResponseEntity<?> list(Locale locale, Users user) {
		return ResponseEntity.ok(goalStatusRepository.findAll());
	}

}
