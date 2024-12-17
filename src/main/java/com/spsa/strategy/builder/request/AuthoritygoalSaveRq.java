package com.spsa.strategy.builder.request;


import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;
import com.spsa.strategy.model.Authoritygoals;

public class AuthoritygoalSaveRq {
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String id;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String goal;

	private Integer yearlyweight;

	private Integer yearlyexpectedweight;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private Date deadline;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String status;

	public Authoritygoals returnAuthoritygoals(String username) {
		Authoritygoals authoritygoals = new Authoritygoals();
		authoritygoals.setDate_time(new Date());
		authoritygoals.setId(this.id);
		authoritygoals.setGoal(this.goal);
		authoritygoals.setYearlyweight(this.yearlyweight);
		authoritygoals.setYearlyexpectedweight(this.yearlyexpectedweight);
		authoritygoals.setDeadline(this.deadline);
		authoritygoals.setStatus(this.status);
		authoritygoals.setUsername(username);
		return authoritygoals;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public Integer getYearlyweight() {
		return yearlyweight;
	}

	public void setYearlyweight(Integer yearlyweight) {
		this.yearlyweight = yearlyweight;
	}

	public Integer getYearlyexpectedweight() {
		return yearlyexpectedweight;
	}

	public void setYearlyexpectedweight(Integer yearlyexpectedweight) {
		this.yearlyexpectedweight = yearlyexpectedweight;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
