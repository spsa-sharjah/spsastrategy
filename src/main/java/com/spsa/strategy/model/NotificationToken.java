package com.spsa.strategy.model;

import java.util.Date;
import java.util.Map;

import com.spsa.strategy.config.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificationtoken")
public class NotificationToken {
    @Id
    private String id;

    @Column(unique = true)
    private String token;

    private String username;

    private String useragent;

    private String platform;

    private String timezone;

    private String clientip;
    
    private Date datetime;

	public NotificationToken() {
		super();
	}
	public NotificationToken(String id, String token, String username, String clientip, Map<String, String> payload) {
		super();
		this.id = id;
		this.token = token;
		this.username = username;
		this.clientip = clientip;
		this.datetime = new Date();
		if (payload.containsKey(Constants.USERAGENT_PARAM))
			this.useragent = payload.get(Constants.USERAGENT_PARAM);
		if (payload.containsKey(Constants.PLATFORM_PARAM))
			this.platform = payload.get(Constants.PLATFORM_PARAM);
		if (payload.containsKey(Constants.TIMEZONE_PARAM))
			this.timezone = payload.get(Constants.TIMEZONE_PARAM);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
}
