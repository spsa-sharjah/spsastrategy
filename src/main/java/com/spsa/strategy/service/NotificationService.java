package com.spsa.strategy.service;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface NotificationService {

    public void registerToken(String token);

    public void sendNotification(String title, String message) throws FirebaseMessagingException;

}