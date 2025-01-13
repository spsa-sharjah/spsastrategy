package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "authoritygoals", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Authoritygoals {
	@Id
	@Size(max = 500)
	private String id;

	private String goal;

	private String goalar;

	private int yearlyweight;
    
	private int yearlyexpectedweight;

	private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date deadline;

	private String status;

	private String year;

	private String quarter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date fromdate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date todate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date date_time;

	@Size(max = 50)
	private String userrole;
	
	public Authoritygoals() {
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

	public int getYearlyweight() {
		return yearlyweight;
	}

	public void setYearlyweight(int yearlyweight) {
		this.yearlyweight = yearlyweight;
	}

	public int getYearlyexpectedweight() {
		return yearlyexpectedweight;
	}

	public void setYearlyexpectedweight(int yearlyexpectedweight) {
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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}
}
