package com.spsa.strategy.builder.request;


import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.Constants;
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

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String year;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String fromdate;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String todate;
    
	private List<String> roles;

	public Authoritygoals returnAuthoritygoals(String username, String userrole, List<String> authorizedapis) {
		Authoritygoals goal = new Authoritygoals();
		goal.setDate_time(new Date());
		goal.setId(this.id);
		goal.setGoal(this.goal);
		goal.setGoalar(this.goalar);
		goal.setYearlyweight(Utils.concertStringtoInteger(this.yearlyweight));
		if (authorizedapis.contains(Constants.UpdateExpectedWeight))
			goal.setYearlyexpectedweight(Utils.concertStringtoInteger(this.yearlyexpectedweight));
		goal.setDeadline(Utils.convertStringToDate(this.deadline, null));
		goal.setStatus(this.status);
		goal.setUsername(username);
		goal.setYear(this.year);
		goal.setFromdate(Utils.convertStringToDate(this.fromdate, null));
		goal.setTodate(Utils.convertStringToDate(this.todate, null));
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
