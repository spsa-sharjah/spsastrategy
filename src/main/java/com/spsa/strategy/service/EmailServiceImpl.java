package com.spsa.strategy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.spsa.strategy.builder.request.EmailDetailsRq;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;


@Service
public class EmailServiceImpl implements EmailService {

	@Value("${spring.mail.from}") 
	private String sender;

	@Value("${spring.mail.host}") 
	private String host;

	@Value("${spring.mail.port}") 
	private String port;

	@Value("${spring.mail.username}") 
	private String username;

	@Value("${spring.mail.password}") 
	private String password;

	@Value("${spring.mail.support}") 
	private String support;

	@Value("${spring.mail.auth}") 
	private String auth;

	@Value("${spring.mail.starttls.enable}") 
	private String enable;
	
	public boolean sendSimpleMail(EmailDetailsRq details) {
		  
		try {
			

			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", enable);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
		    props.put("mail.smtp.auth", auth);
		    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
	
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			session.setDebug(true);
			
			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress(sender));

			try {
				InternetAddress[] toAddress = new InternetAddress[1];
				toAddress[0] = new InternetAddress(details.getRecipient());
				message.setRecipients(RecipientType.TO, toAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				InternetAddress[] bccAddress = new InternetAddress[1];
				bccAddress[0] = new InternetAddress(support);
				message.setRecipients(RecipientType.BCC, bccAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			message.setSubject(details.getSubject());

			message.setContent(details.getMsgBody(), "text/html");

			Transport.send(message);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
