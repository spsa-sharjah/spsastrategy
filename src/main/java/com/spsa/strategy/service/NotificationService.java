package com.spsa.strategy.service;

import java.util.Locale;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.spsa.strategy.model.Users;

import jakarta.servlet.http.HttpServletRequest;

public interface NotificationService {

    public void registertoken(HttpServletRequest request, Users user, Map<String, String> payload);

	public boolean sendnotification(String username, Map<String, String> payload);

	public ResponseEntity<?> list(Locale locale, Integer page, Integer size, String search, String sortcolumn,
			Boolean descending, Integer draw, Users user, boolean isadmin, Boolean assender);

	public ResponseEntity<?> newnotifications(Locale locale, Users user);

	public ResponseEntity<?> notificationdetails(Locale locale, Users user, Long id, boolean isadmin);

	public ResponseEntity<?> notificationseen(Locale locale, Users user, Long id);

}