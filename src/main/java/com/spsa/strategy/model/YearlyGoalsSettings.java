package com.spsa.strategy.model;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "yearlygoalssettings")
public class YearlyGoalsSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year", nullable = false, length = 45)
    private String year;

    @Column(name = "quarter1_date")
    private Timestamp quarter1Date;

    @Column(name = "quarter2_date")
    private Timestamp quarter2Date;

    @Column(name = "quarter3_date")
    private Timestamp quarter3Date;

    @Column(name = "quarter4_date")
    private Timestamp quarter4Date;

    @Column(name = "deadline_notification_days")
    private Integer deadlineNotificationDays;

    @Column(name = "status", length = 200)
    private String status;


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

    public Timestamp getQuarter1Date() {
        return quarter1Date;
    }

    public void setQuarter1Date(Timestamp quarter1Date) {
        this.quarter1Date = quarter1Date;
    }

    public Timestamp getQuarter2Date() {
        return quarter2Date;
    }

    public void setQuarter2Date(Timestamp quarter2Date) {
        this.quarter2Date = quarter2Date;
    }

    public Timestamp getQuarter3Date() {
        return quarter3Date;
    }

    public void setQuarter3Date(Timestamp quarter3Date) {
        this.quarter3Date = quarter3Date;
    }

    public Timestamp getQuarter4Date() {
        return quarter4Date;
    }

    public void setQuarter4Date(Timestamp quarter4Date) {
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
}
