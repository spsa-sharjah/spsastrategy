package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "departmentgoals", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Departmentgoals {
	@Id
	@Size(max = 500)
	private String id;

	@Size(max = 500)
	private String authgoalid;
	
	private String goal;

	private String goalar;

	private int yearlyweight;
    
	private Integer yearlyexpectedweight;

	private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date deadline;

	private String status;

	private String reason;

	private String solution;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date date_time;

	@Size(max = 50)
	private String userrole;

//	weight calculation fields - per parent goal id
	@Transient
	private Integer remainingweight;

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

	public int getYearlyweight() {
		return yearlyweight;
	}

	public void setYearlyweight(int yearlyweight) {
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

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGoalar() {
		return goalar;
	}

	public void setGoalar(String goalar) {
		this.goalar = goalar;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	public Integer getRemainingweight() {
		return remainingweight;
	}

	public void setRemainingweight(Integer remainingweight) {
		this.remainingweight = remainingweight;
	}
}
