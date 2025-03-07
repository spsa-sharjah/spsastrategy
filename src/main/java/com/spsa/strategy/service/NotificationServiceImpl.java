package com.spsa.strategy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.spsa.strategy.model.NotificationToken;
import com.spsa.strategy.repository.NotificationTokenRepository;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	
	@Autowired
    private NotificationTokenRepository repository;

    public void registerToken(String token) {
        if (repository.findAll().stream().noneMatch(t -> t.getToken().equals(token))) {
            NotificationToken notificationToken = new NotificationToken();
            notificationToken.setToken(token);
            repository.save(notificationToken);
        }
    }

    public void sendNotification(String title, String message) throws FirebaseMessagingException {
        List<NotificationToken> tokens = repository.findAll();
        for (NotificationToken token : tokens) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(message)
                    .build();

            Message firebaseMessage = Message.builder()
                    .setToken(token.getToken())
                    .setNotification(notification)
                    .build();

            FirebaseMessaging.getInstance().send(firebaseMessage);
        }
    }
}