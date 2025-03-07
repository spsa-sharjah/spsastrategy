package com.spsa.strategy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.spsa.strategy.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
	@Autowired
    private NotificationService service;

    @PostMapping("/register")
    public ResponseEntity<?> registerToken(@RequestBody Map<String, String> payload) {
        service.registerToken(payload.get("token"));
        return ResponseEntity.ok("Token registered.");
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, String> payload) {
        try {
            service.sendNotification(payload.get("title"), payload.get("message"));
            return ResponseEntity.ok("Notification sent.");
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }
}
