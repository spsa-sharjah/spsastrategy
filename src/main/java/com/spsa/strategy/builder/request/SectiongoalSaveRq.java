package com.spsa.strategy.builder.request;


import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.model.Sectiongoals;

public class SectiongoalSaveRq {
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String id;
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String depgoalid;

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

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String reason;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String solution;

	public Sectiongoals returnSectiongoals(String username, String userrole) {
		Sectiongoals goal = new Sectiongoals();
		goal.setDate_time(new Date());
		goal.setDepgoalid(this.depgoalid);
		goal.setId(this.id);
		goal.setGoal(this.goal);
		goal.setGoalar(this.goalar);
		goal.setYearlyweight(Utils.concertStringtoInteger(this.yearlyweight));
		goal.setYearlyexpectedweight(Utils.concertStringtoInteger(this.yearlyexpectedweight));
		goal.setDeadline(Utils.convertStringToDate(this.deadline, null));
		goal.setStatus(this.status);
		goal.setUsername(username);
		goal.setReason(this.reason);
		goal.setSolution(this.solution);
		goal.setUserrole(userrole);
		return goal;
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

	public String getDepgoalid() {
		return depgoalid;
	}

	public void setDepgoalid(String depgoalid) {
		this.depgoalid = depgoalid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}
}
