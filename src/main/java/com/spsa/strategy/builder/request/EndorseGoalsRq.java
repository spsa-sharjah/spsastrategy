package com.spsa.strategy.builder.request;

import java.util.List;

public class EndorseGoalsRq {
	private String authgoalid;
	
	private List<EndorseRq> choosendepgoallist;

	public String getAuthgoalid() {
		return authgoalid;
	}

	public void setAuthgoalid(String authgoalid) {
		this.authgoalid = authgoalid;
	}

	public List<EndorseRq> getChoosendepgoallist() {
		return choosendepgoallist;
	}

	public void setChoosendepgoallist(List<EndorseRq> choosendepgoallist) {
		this.choosendepgoallist = choosendepgoallist;
	}
}
