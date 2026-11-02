package com.likhitha.job_tracker_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.likhitha.job_tracker_api.model.JobStatus;

public class JobResponse {

    private Long id;
    private String company;
    private String position;
    private String location;
    private JobStatus status;
    private LocalDate appliedDate;
    private String jobUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobResponse() {
    }

    public JobResponse(Long id, String company, String position, String location,
            JobStatus status, LocalDate appliedDate, String jobUrl, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.location = location;
        this.status = status;
        this.appliedDate = appliedDate;
        this.jobUrl = jobUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}