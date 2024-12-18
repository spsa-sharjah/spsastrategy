package com.spsa.strategy.builder.request;


import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Authoritygoals;

public class AuthoritygoalSaveRq {
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String id;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String goal;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String goalar;

	private String yearlyweight;

	private String yearlyexpectedweight;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String deadline;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String status;

	public Authoritygoals returnAuthoritygoals(String username) {
		Authoritygoals authoritygoals = new Authoritygoals();
		authoritygoals.setDate_time(new Date());
		authoritygoals.setId(this.id);
		authoritygoals.setGoal(this.goal);
		authoritygoals.setGoalar(this.goalar);
		authoritygoals.setYearlyweight(Utils.concertStringtoInteger(this.yearlyweight));
		authoritygoals.setYearlyexpectedweight(Utils.concertStringtoInteger(this.yearlyexpectedweight));
		authoritygoals.setDeadline(Utils.convertStringToDate(this.deadline, null));
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

	public String getYearlyweight() {
		return yearlyweight;
	}

	public void setYearlyweight(String yearlyweight) {
		this.yearlyweight = yearlyweight;
	}

	public String getYearlyexpectedweight() {
		return yearlyexpectedweight;
	}

	public void setYearlyexpectedweight(String yearlyexpectedweight) {
		this.yearlyexpectedweight = yearlyexpectedweight;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoalar() {
		return goalar;
	}

	public void setGoalar(String goalar) {
		this.goalar = goalar;
	}
}
