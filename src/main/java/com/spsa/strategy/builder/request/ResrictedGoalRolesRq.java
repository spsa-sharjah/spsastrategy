package com.spsa.strategy.builder.request;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;

public class ResrictedGoalRolesRq {

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String goalid;
    
	private List<String> roles;

	public String getGoalid() {
		return goalid;
	}

	public void setGoalid(String goalid) {
		this.goalid = goalid;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
