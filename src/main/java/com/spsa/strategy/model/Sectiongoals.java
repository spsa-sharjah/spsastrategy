package com.spsa.strategy.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "sectiongoals", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Sectiongoals {
	@Id
	@Size(max = 500)
	private String id;

	@Size(max = 500)
	private String depgoalid;
	
	private String goal;

	private int yearlyweight;
    
	private Integer yearlyexpectedweight;

	private String username;

	private Date deadline;

	private String status;

	private String reason;

	private String solution;

	private Date date_time;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
