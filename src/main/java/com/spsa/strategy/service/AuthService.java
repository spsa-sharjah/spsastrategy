package com.spsa.strategy.service;

import java.util.Locale;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.builder.request.ResrictedGoalRolesRq;
import com.spsa.strategy.model.Users;

import jakarta.validation.Valid;

public interface AuthService {

	ResponseEntity<?> callAuth(String apikey, String apisecret, String username, String token, String url, String lang);

	ResponseEntity<?> rolegoalsaccesssave(Locale locale, Users user, @Valid ResrictedGoalRolesRq req);

	ResponseEntity<?> rolegoalaccesslist(Locale locale, Users user, String goalid);

}
