package com.spsa.strategy.model;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spsa.strategy.builder.request.YearlySettingsRq;
import com.spsa.strategy.config.Constants;
import com.spsa.strategy.config.Utils;
import com.spsa.strategy.enumeration.YearlyGoalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "yearlygoalssettings", uniqueConstraints = {@UniqueConstraint(columnNames = "year")})
public class YearlyGoalsSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false, length = 45, unique = true)
    private String year;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    @Column(name = "quarter1_date")
    private Date quarter1Date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    @Column(name = "quarter2_date")
    private Date quarter2Date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    @Column(name = "quarter3_date")
    private Date quarter3Date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    @Column(name = "quarter4_date")
    private Date quarter4Date;

    @Column(name = "deadline_notification_days")
    private Integer deadlineNotificationDays;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    @Column(name = "endorsement_deadline")
    private Date endorsementDeadline;

    @Column(name = "status", length = 200)
    private String status;

    @Column(name = "skip_endorsement")
    private boolean skipendorsement;

    public YearlyGoalsSettings() {
		super();
    }
    public YearlyGoalsSettings(String year) {
		super();
		this.year = year;
		int yr = Integer.parseInt(year); 
		this.quarter1Date = getQuarterDeadline(yr, Calendar.MARCH);
		this.quarter2Date = getQuarterDeadline(yr, Calendar.JUNE);
		this.quarter3Date = getQuarterDeadline(yr, Calendar.SEPTEMBER);
		this.quarter4Date = getQuarterDeadline(yr, Calendar.DECEMBER);
		this.deadlineNotificationDays = Constants.DEFAULT_DEADLINE_NOTIFICATION_DAYS;
		this.endorsementDeadline = Utils.addMonthsToDate(this.quarter1Date, Constants.DEFAULT_ENDORSEMENT_DEADLINE_NBR_MONTH);
		this.status = YearlyGoalStatus.New.name();
		this.skipendorsement = false;
	}

    private Date getQuarterDeadline(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1, 23, 59, 59);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

	public YearlyGoalsSettings(YearlySettingsRq req, boolean authorizedtoskipendorsement) {
		super();
		this.year = req.getYear();
		this.quarter1Date = Utils.convertStringToDate(req.getQuarter1Date(), null);
		this.quarter2Date = Utils.convertStringToDate(req.getQuarter2Date(), null);
		this.quarter3Date = Utils.convertStringToDate(req.getQuarter3Date(), null);
		this.quarter4Date = Utils.convertStringToDate(req.getQuarter4Date(), null);
		this.deadlineNotificationDays = Utils.concertStringtoInteger(req.getDeadlineNotificationDays());
		this.endorsementDeadline =  Utils.convertStringToDate(req.getEndorsementDeadline(), null);
		this.skipendorsement = authorizedtoskipendorsement ? req.isSkipendorsement() : false;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getQuarter1Date() {
        return quarter1Date;
    }

    public void setQuarter1Date(Date quarter1Date) {
        this.quarter1Date = quarter1Date;
    }

    public Date getQuarter2Date() {
        return quarter2Date;
    }

    public void setQuarter2Date(Date quarter2Date) {
        this.quarter2Date = quarter2Date;
    }

    public Date getQuarter3Date() {
        return quarter3Date;
    }

    public void setQuarter3Date(Date quarter3Date) {
        this.quarter3Date = quarter3Date;
    }

    public Date getQuarter4Date() {
        return quarter4Date;
    }

    public void setQuarter4Date(Date quarter4Date) {
        this.quarter4Date = quarter4Date;
    }

    public Integer getDeadlineNotificationDays() {
        return deadlineNotificationDays;
    }

    public void setDeadlineNotificationDays(Integer deadlineNotificationDays) {
        this.deadlineNotificationDays = deadlineNotificationDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
	public Date getEndorsementDeadline() {
		return endorsementDeadline;
	}
	public void setEndorsementDeadline(Date endorsementDeadline) {
		this.endorsementDeadline = endorsementDeadline;
	}

	public boolean isSkipendorsement() {
		return skipendorsement;
	}

	public void setSkipendorsement(boolean skipendorsement) {
		this.skipendorsement = skipendorsement;
	}
}
