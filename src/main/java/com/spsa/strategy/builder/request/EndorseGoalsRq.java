package com.spsa.strategy.builder.request;

import java.util.List;

public class EndorseGoalsRq {
	private String authgoalid;
	
	private List<EndorseRq> depgoallist;

	public String getAuthgoalid() {
		return authgoalid;
	}

	public void setAuthgoalid(String authgoalid) {
		this.authgoalid = authgoalid;
	}

	public List<EndorseRq> getDepgoallist() {
		return depgoallist;
	}

	public void setDepgoallist(List<EndorseRq> depgoallist) {
		this.depgoallist = depgoallist;
	}
}
