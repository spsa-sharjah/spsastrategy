package com.spsa.strategy.builder.request;


import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;
import com.spsa.strategy.model.Departmentgoals;

import jakarta.validation.constraints.NotNull;

public class DepartmentgoalSaveRq {
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String id;
	
    @NotNull
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String authgoalid;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String goal;

	private Integer yearlyweight;

	private Integer yearlyexpectedweight;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private Date deadline;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String status;

	public Departmentgoals returnDepartmentgoals(String username) {
		Departmentgoals goal = new Departmentgoals();
		goal.setDate_time(new Date());
		goal.setAuthgoalid(this.authgoalid);
		goal.setId(this.id);
		goal.setGoal(this.goal);
		goal.setYearlyweight(this.yearlyweight);
		goal.setYearlyexpectedweight(this.yearlyexpectedweight);
		goal.setDeadline(this.deadline);
		goal.setStatus(this.status);
		goal.setUsername(username);
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
