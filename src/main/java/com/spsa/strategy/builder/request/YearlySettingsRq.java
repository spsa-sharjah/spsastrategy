package com.spsa.strategy.builder.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.spsa.strategy.config.SanitizedStringDeserializer;

import jakarta.validation.constraints.NotNull;

public class YearlySettingsRq {
	
	@NotNull
    @JsonDeserialize(using = SanitizedStringDeserializer.class)
	private String year;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String quarter1Date;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String quarter2Date;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String quarter3Date;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String quarter4Date;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String deadlineNotificationDays;

    @JsonDeserialize(using = SanitizedStringDeserializer.class)
    private String endorsementDeadline;

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter1Date() {
		return quarter1Date;
	}

	public void setQuarter1Date(String quarter1Date) {
		this.quarter1Date = quarter1Date;
	}

	public String getQuarter2Date() {
		return quarter2Date;
	}

	public void setQuarter2Date(String quarter2Date) {
		this.quarter2Date = quarter2Date;
	}

	public String getQuarter3Date() {
		return quarter3Date;
	}

	public void setQuarter3Date(String quarter3Date) {
		this.quarter3Date = quarter3Date;
	}

	public String getQuarter4Date() {
		return quarter4Date;
	}

	public void setQuarter4Date(String quarter4Date) {
		this.quarter4Date = quarter4Date;
	}

	public String getDeadlineNotificationDays() {
		return deadlineNotificationDays;
	}

	public void setDeadlineNotificationDays(String deadlineNotificationDays) {
		this.deadlineNotificationDays = deadlineNotificationDays;
	}

	public String getEndorsementDeadline() {
		return endorsementDeadline;
	}

	public void setEndorsementDeadline(String endorsementDeadline) {
		this.endorsementDeadline = endorsementDeadline;
	}
}
