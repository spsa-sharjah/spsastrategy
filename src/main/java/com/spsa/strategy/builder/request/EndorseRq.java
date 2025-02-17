package com.spsa.strategy.builder.request;

public class EndorseRq {

	private String depgoalid;

	private boolean approved;

	private String reason;

	public String getDepgoalid() {
		return depgoalid;
	}

	public void setDepgoalid(String depgoalid) {
		this.depgoalid = depgoalid;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
