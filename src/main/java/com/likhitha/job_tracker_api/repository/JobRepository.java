package com.likhitha.job_tracker_api.repository;

import com.likhitha.job_tracker_api.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JobRepository extends JpaRepository<Job, Long>,JpaSpecificationExecutor<Job> {
    
}