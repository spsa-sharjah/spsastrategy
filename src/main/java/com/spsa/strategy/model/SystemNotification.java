package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "systemnotification")
public class SystemNotification {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String tokenid;

    private String senderusername;

    private String receiverusername;

    private String title;
    
    private String message;

    private boolean saw;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT)
    private Date datetime;

	public SystemNotification() {
		super();
	}

	public SystemNotification(String tokenid, String senderusername, String receiverusername, String title,
			String message) {
		super();
		this.tokenid = tokenid;
		this.senderusername = senderusername;
		this.receiverusername = receiverusername;
		this.title = title;
		this.message = message;
		this.saw = false;
		this.datetime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public String getSenderusername() {
		return senderusername;
	}

	public void setSenderusername(String senderusername) {
		this.senderusername = senderusername;
	}

	public String getReceiverusername() {
		return receiverusername;
	}

	public void setReceiverusername(String receiverusername) {
		this.receiverusername = receiverusername;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSaw() {
		return saw;
	}

	public void setSaw(boolean saw) {
		this.saw = saw;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
