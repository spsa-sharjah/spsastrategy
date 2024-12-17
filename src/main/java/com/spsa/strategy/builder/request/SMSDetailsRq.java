package com.spsa.strategy.builder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;

public class SMSDetailsRq {

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String recipient;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String msgBody;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String captchaToken;
	
	private boolean useAccela;
	
	public SMSDetailsRq() {
		super();
	}
	public String getRecipient() {
		return recipient;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getCaptchaToken() {
		return captchaToken;
	}
	public void setCaptchaToken(String captchaToken) {
		this.captchaToken = captchaToken;
	}
	public boolean isUseAccela() {
		return useAccela;
	}
	public void setUseAccela(boolean useAccela) {
		this.useAccela = useAccela;
	}
}
