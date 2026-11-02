package com.likhitha.job_tracker_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.likhitha.job_tracker_api.specification.JobSpecification;

import com.likhitha.job_tracker_api.dto.JobRequest;
import com.likhitha.job_tracker_api.dto.JobResponse;
import com.likhitha.job_tracker_api.exception.JobNotFoundException;
import com.likhitha.job_tracker_api.model.Job;
import com.likhitha.job_tracker_api.repository.JobRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.likhitha.job_tracker_api.model.JobStatus;

@Service
public class JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Page<JobResponse> getJobs(Pageable pageable) {

        logger.debug("Fetching jobs with pagination: {}", pageable);

        return jobRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public JobResponse getJobById(Long id) {

        logger.debug("Fetching job with id: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Job not found with id: {}", id);
                    return new JobNotFoundException(id);
                });

        return toResponse(job);
    }

    public Page<JobResponse> searchJobs(
            JobStatus status,
            String company,
            String location,
            Pageable pageable) {

        logger.debug(
                "Searching jobs with status: {}, company: {}, location: {}, pageable: {}",
                status, company, location, pageable);

        return jobRepository.findAll(
                Specification
                        .where(JobSpecification.hasStatus(status))
                        .and(JobSpecification.companyContains(company))
                        .and(JobSpecification.locationContains(location)),
                pageable).map(this::toResponse);
    }

    public JobResponse createJob(JobRequest request) {

        logger.info("Creating job application for company: {}, position: {}",
                request.getCompany(), request.getPosition());

        Job job = new Job();

        job.setCompany(request.getCompany());
        job.setPosition(request.getPosition());
        job.setLocation(request.getLocation());
        job.setStatus(request.getStatus());
        job.setAppliedDate(request.getAppliedDate());
        job.setJobUrl(request.getJobUrl());

        Job savedJob = jobRepository.save(job);

        logger.info("Successfully created job with id: {}", savedJob.getId());

        return toResponse(savedJob);
    }

    public JobResponse updateJob(Long id, JobRequest request) {

        logger.info("Updating job with id: {}", id);

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update. Job not found with id: {}", id);
                    return new JobNotFoundException(id);
                });

        existingJob.setCompany(request.getCompany());
        existingJob.setPosition(request.getPosition());
        existingJob.setLocation(request.getLocation());
        existingJob.setStatus(request.getStatus());
        existingJob.setAppliedDate(request.getAppliedDate());
        existingJob.setJobUrl(request.getJobUrl());

        Job savedJob = jobRepository.save(existingJob);

        logger.info("Successfully updated job with id: {}", savedJob.getId());

        return toResponse(savedJob);
    }

    public void deleteJob(Long id) {

        logger.info("Deleting job with id: {}", id);

        if (!jobRepository.existsById(id)) {
            logger.warn("Cannot delete. Job not found with id: {}", id);
            throw new JobNotFoundException(id);
        }

        jobRepository.deleteById(id);

        logger.info("Successfully deleted job with id: {}", id);
    }

    private JobResponse toResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getCompany(),
                job.getPosition(),
                job.getLocation(),
                job.getStatus(),
                job.getAppliedDate(),
                job.getJobUrl(),
                job.getCreatedAt(),
                job.getUpdatedAt());
    }
}