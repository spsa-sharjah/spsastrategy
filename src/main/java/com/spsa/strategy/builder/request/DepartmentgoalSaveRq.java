package com.spsa.strategy.builder.request;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.CustomAction;
import com.spsa.strategy.enumeration.Menuauthid;
import com.spsa.strategy.model.Departmentgoals;
import com.spsa.strategy.model.Users;

import jakarta.validation.constraints.Size;

public class DepartmentgoalSaveRq {
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String id;
	
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String authgoalid;

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
    
	private List<String> roles;
	
	private boolean chosenbydefault;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String endorsementreason;

	@Size(max = 200)
	private String team;

	public Departmentgoals returnDepartmentgoals(String username, Users user, Departmentgoals deptgoal) {
		Departmentgoals goal = new Departmentgoals();
		goal.setDate_time(new Date());
		goal.setAuthgoalid(this.authgoalid);
		goal.setId(this.id);
		goal.setGoal(this.goal);
		goal.setGoalar(this.goalar);
		goal.setYearlyweight(Utils.concertStringtoInteger(this.yearlyweight));
		goal.setYearlyexpectedweight(Utils.concertStringtoInteger(this.yearlyexpectedweight));
		goal.setDeadline(Utils.convertStringToDate(this.deadline, null));
		goal.setStatus(this.status);
		goal.setUsername(username);
		
		if (deptgoal != null) goal.setReason(deptgoal.getReason()); // save old data
		if (Utils.isapiauthorized(CustomAction.UpdateReason.name(), Menuauthid.managedepartmentgoals.name(), user.getAuthorizedapis())) 
			goal.setReason(this.reason);

		if (deptgoal != null) goal.setSolution(deptgoal.getSolution());
		if (Utils.isapiauthorized(CustomAction.UpdateSolution.name(), Menuauthid.managedepartmentgoals.name(), user.getAuthorizedapis())) 
			goal.setSolution(this.solution);
			
		goal.setUserrole(user.getUser_role());
		goal.setTeam(user.getTeam());
		goal.setChosenbydefault(chosenbydefault);
		goal.setTeam(user.getTeam());
		goal.setEndorsementreason(this.endorsementreason);
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

	public String getAuthgoalid() {
		return authgoalid;
	}

	public void setAuthgoalid(String authgoalid) {
		this.authgoalid = authgoalid;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean isChosenbydefault() {
		return chosenbydefault;
	}

	public void setChosenbydefault(boolean chosenbydefault) {
		this.chosenbydefault = chosenbydefault;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getEndorsementreason() {
		return endorsementreason;
	}

	public void setEndorsementreason(String endorsementreason) {
		this.endorsementreason = endorsementreason;
	}
}
