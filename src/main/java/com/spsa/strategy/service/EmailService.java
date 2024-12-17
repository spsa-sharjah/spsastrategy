package com.spsa.strategy.service;

import com.spsa.strategy.builder.request.EmailDetailsRq;

public interface EmailService {

	boolean sendSimpleMail(EmailDetailsRq details);

}
