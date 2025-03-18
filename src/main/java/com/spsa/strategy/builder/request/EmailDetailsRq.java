package com.spsa.strategy.builder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.SanitizedStringDeserializer;

public class EmailDetailsRq {

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String recipient;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String msgBody;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String subject;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String captchaToken;
	
	public EmailDetailsRq() {
		super();
	}
	
	public EmailDetailsRq(String recipient, String msgBody, String subject) {
		super();
		this.recipient = recipient;
		this.msgBody = msgBody;
		this.subject = subject;
	}

	public String getRecipient() {
		return recipient;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public String getSubject() {
		return Constants.SUBJECT + subject;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCaptchaToken() {
		return captchaToken;
	}
}
