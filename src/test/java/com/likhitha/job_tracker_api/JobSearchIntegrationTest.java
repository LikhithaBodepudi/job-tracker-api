
package com.likhitha.job_tracker_api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.likhitha.job_tracker_api.dto.JobResponse;
import com.likhitha.job_tracker_api.model.Job;
import com.likhitha.job_tracker_api.model.JobStatus;
import com.likhitha.job_tracker_api.repository.JobRepository;
import com.likhitha.job_tracker_api.service.JobService;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JobSearchIntegrationTest {

        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:18");

        @Autowired
        private JobService jobService;

        @Autowired
        private JobRepository jobRepository;

        @BeforeEach
        void setUp() {
                jobRepository.deleteAll();

                jobRepository.save(createJob(
                                "Google", "Software Engineer", "California",
                                JobStatus.APPLIED));

                jobRepository.save(createJob(
                                "Google LLC", "Backend Developer", "New York",
                                JobStatus.INTERVIEW));

                jobRepository.save(createJob(
                                "Microsoft", "Java Developer", "Redmond",
                                JobStatus.APPLIED));

                jobRepository.save(createJob(
                                "Amazon", "Software Engineer", "Seattle",
                                JobStatus.OFFER));
        }

        @Test
        void searchByCompanyIsCaseInsensitiveAndPartial() {

                Page<JobResponse> result = jobService.searchJobs(
                                null, "GOO", null, PageRequest.of(0, 10));

                assertEquals(2, result.getTotalElements());
        }

        @Test
        void searchByLocationIsCaseInsensitiveAndPartial() {

                Page<JobResponse> result = jobService.searchJobs(
                                null, null, "red", PageRequest.of(0, 10));

                assertEquals(1, result.getTotalElements());
                assertEquals("Redmond", result.getContent().get(0).getLocation());
        }

        @Test
        void searchByStatusReturnsOnlyMatchingJobs() {

                Page<JobResponse> result = jobService.searchJobs(
                                JobStatus.APPLIED, null, null, PageRequest.of(0, 10));

                assertEquals(2, result.getTotalElements());
        }

        @Test
        void searchWithMultipleFiltersUsesAndLogic() {

                Page<JobResponse> result = jobService.searchJobs(
                                JobStatus.INTERVIEW,
                                "google",
                                "new",
                                PageRequest.of(0, 10));

                assertEquals(1, result.getTotalElements());
                assertEquals("Google LLC", result.getContent().get(0).getCompany());
        }

        @Test
        void searchSupportsPagination() {

                Page<JobResponse> firstPage = jobService.searchJobs(
                                null,
                                null,
                                null,
                                PageRequest.of(0, 2, Sort.by("company").ascending()));

                Page<JobResponse> secondPage = jobService.searchJobs(
                                null,
                                null,
                                null,
                                PageRequest.of(1, 2, Sort.by("company").ascending()));

                assertEquals(4, firstPage.getTotalElements());
                assertEquals(2, firstPage.getContent().size());
                assertEquals(2, secondPage.getContent().size());

                assertEquals("Amazon", firstPage.getContent().get(0).getCompany());
                assertEquals("Google", firstPage.getContent().get(1).getCompany());
                assertEquals("Google LLC", secondPage.getContent().get(0).getCompany());
                assertEquals("Microsoft", secondPage.getContent().get(1).getCompany());
        }

        private Job createJob(
                        String company,
                        String position,
                        String location,
                        JobStatus status) {

                Job job = new Job();

                job.setCompany(company);
                job.setPosition(position);
                job.setLocation(location);
                job.setStatus(status);
                job.setAppliedDate(LocalDate.of(2026, 1, 15));

                return job;
        }
}
