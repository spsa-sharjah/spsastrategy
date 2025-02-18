package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "evidence_reply", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class EvidenceReply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long evidenceid;
	
	private String username;
	
	private String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date date_time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEvidenceid() {
		return evidenceid;
	}

	public void setEvidenceid(Long evidenceid) {
		this.evidenceid = evidenceid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
