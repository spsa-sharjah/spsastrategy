package com.spsa.strategy.builder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;

import jakarta.validation.constraints.NotNull;

public class EvidenceCommentSaveRq {

	private Long id;
	@NotNull
	private Long evidenceid;
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String comment;
	
	public EvidenceCommentSaveRq() {
		super();
	}
	
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
	
}
