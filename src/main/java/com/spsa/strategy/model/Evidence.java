package com.spsa.strategy.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.config.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "evidence", uniqueConstraints = { @UniqueConstraint(columnNames = "id") })
public class Evidence {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	
	private String comment;
    
	private String goalid;
    
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
	private Date date_time;

    @Transient 
	private Integer filesnbr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getGoalid() {
		return goalid;
	}

	public void setGoalid(String goalid) {
		this.goalid = goalid;
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

	public Integer getFilesnbr() {
		return filesnbr;
	}

	public void setFilesnbr(Integer filesnbr) {
		this.filesnbr = filesnbr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
