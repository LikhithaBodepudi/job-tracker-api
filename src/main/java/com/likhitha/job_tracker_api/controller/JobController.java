package com.likhitha.job_tracker_api.controller;

import com.likhitha.job_tracker_api.dto.JobRequest;
import com.likhitha.job_tracker_api.dto.JobResponse;
import com.likhitha.job_tracker_api.model.JobStatus;
import com.likhitha.job_tracker_api.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Set;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Management", description = "APIs for creating, viewing, updating, deleting, and searching job applications")
public class JobController {

        private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
                        "id",
                        "company",
                        "position",
                        "location",
                        "status",
                        "appliedDate",
                        "jobUrl",
                        "createdAt",
                        "updatedAt");

        private final JobService jobService;

        public JobController(JobService jobService) {
                this.jobService = jobService;
        }

        @Operation(summary = "Search job applications", description = "Search job applications using optional status, company, and location filters with pagination and sorting")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Job applications retrieved successfully")
        })
        @GetMapping
        public Page<JobResponse> getJobs(
                        @RequestParam(required = false) JobStatus status,
                        @RequestParam(required = false) String company,
                        @RequestParam(required = false) String location,

                        @RequestParam(required = false) Integer page,
                        @RequestParam(required = false) Integer size,

                        @PageableDefault(page = 0, size = 5, sort = "appliedDate", direction = Sort.Direction.DESC) Pageable pageable) {

                if (page != null && page < 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Page number cannot be negative");
                }

                if (size != null && size < 1) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Page size must be at least 1");
                }

                boolean invalidSortField = pageable.getSort()
                                .stream()
                                .anyMatch(order -> !ALLOWED_SORT_FIELDS.contains(order.getProperty()));

                if (invalidSortField) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid sort field. Allowed fields: "
                                                        + String.join(", ", ALLOWED_SORT_FIELDS));
                }

                return jobService.searchJobs(
                                status,
                                company,
                                location,
                                pageable);

        }

        @Operation(summary = "Get job application by ID", description = "Returns a single job application using its unique ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Job application found"),
                        @ApiResponse(responseCode = "404", description = "Job application not found")
        })
        @GetMapping("/{id}")
        public JobResponse getJobById(@PathVariable Long id) {
                return jobService.getJobById(id);
        }

        @Operation(summary = "Create a job application", description = "Creates a new job application")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Job application created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid job application data")
        })
        @PostMapping
        public ResponseEntity<JobResponse> createJob(
                        @Valid @RequestBody JobRequest request) {

                JobResponse createdJob = jobService.createJob(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(createdJob);
        }

        @Operation(summary = "Update a job application", description = "Updates an existing job application using its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Job application updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid job application data"),
                        @ApiResponse(responseCode = "404", description = "Job application not found")
        })
        @PutMapping("/{id}")
        public JobResponse updateJob(
                        @PathVariable Long id,
                        @Valid @RequestBody JobRequest request) {

                return jobService.updateJob(id, request);
        }

        @Operation(summary = "Delete a job application", description = "Deletes a job application using its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Job application deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Job application not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteJob(@PathVariable Long id) {

                jobService.deleteJob(id);

                return ResponseEntity.noContent().build();
        }
}