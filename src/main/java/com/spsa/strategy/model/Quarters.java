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
@Table(name = "quarters", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Quarters {
	@Id
	@Size(max = 500)
	private String id;

	private boolean name;
	private boolean year;

	private Integer quarternumber;
	private String goalid;
	private Integer expectedweight;
	private Integer weight;
	private String status;
	private String deadline;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT)
	private Date date_time;

	public Quarters() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isName() {
		return name;
	}

	public void setName(boolean name) {
		this.name = name;
	}

	public boolean isYear() {
		return year;
	}

	public void setYear(boolean year) {
		this.year = year;
	}

	public Integer getQuarternumber() {
		return quarternumber;
	}

	public void setQuarternumber(Integer quarternumber) {
		this.quarternumber = quarternumber;
	}

	public String getGoalid() {
		return goalid;
	}

	public void setGoalid(String goalid) {
		this.goalid = goalid;
	}

	public Integer getExpectedweight() {
		return expectedweight;
	}

	public void setExpectedweight(Integer expectedweight) {
		this.expectedweight = expectedweight;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	
}
