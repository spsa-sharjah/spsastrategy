package com.spsa.strategy.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
