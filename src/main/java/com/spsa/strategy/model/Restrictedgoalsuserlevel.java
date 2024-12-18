package com.spsa.strategy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@IdClass(Restrictedgoalsuserlevel.class)
@Table(name = "restrictedgoalsuserlevel")
public class Restrictedgoalsuserlevel {
	@Id
	@Size(max = 500)
	private String goalid;

	@Id
	@Size(max = 500)
	private String userlevelid;

	public Restrictedgoalsuserlevel() {
	}

	public String getGoalid() {
		return goalid;
	}

	public void setGoalid(String goalid) {
		this.goalid = goalid;
	}

	public String getUserlevelid() {
		return userlevelid;
	}

	public void setUserlevelid(String userlevelid) {
		this.userlevelid = userlevelid;
	}
}
