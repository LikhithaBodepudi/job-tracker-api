package com.likhitha.job_tracker_api.specification;

import com.likhitha.job_tracker_api.model.Job;
import com.likhitha.job_tracker_api.model.JobStatus;

import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> hasStatus(JobStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Job> companyContains(String company) {

        return (root, query, criteriaBuilder) -> {

            if (company == null || company.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("company")),
                    "%" + company.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Job> locationContains(String location) {

        return (root, query, criteriaBuilder) -> {

            if (location == null || location.isBlank()) {
                return null;
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    "%" + location.toLowerCase() + "%"
            );
        };
    }
}
