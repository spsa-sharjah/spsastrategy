package com.spsa.strategy.builder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;

import jakarta.validation.constraints.NotNull;

public class EvidenceSaveRq {

	private Long id;
	@NotNull
	private String goalid;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String comment;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String status;
	
	public EvidenceSaveRq() {
		super();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoalid() {
		return goalid;
	}

	public void setGoalid(String goalid) {
		this.goalid = goalid;
	}
}
