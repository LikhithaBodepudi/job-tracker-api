package com.likhitha.job_tracker_api.service;

import com.likhitha.job_tracker_api.dto.JobResponse;
import com.likhitha.job_tracker_api.model.Job;
import com.likhitha.job_tracker_api.model.JobStatus;
import com.likhitha.job_tracker_api.repository.JobRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.likhitha.job_tracker_api.exception.JobNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.likhitha.job_tracker_api.dto.JobRequest;

public class JobServiceTest {

        @Mock
        private JobRepository jobRepository;

        @InjectMocks
        private JobService jobService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void getJobById_shouldReturnJobResponse() {

                Job job = new Job();

                job.setId(1L);
                job.setCompany("Amazon");
                job.setPosition("Software Engineer");
                job.setLocation("Dallas, TX");
                job.setStatus(JobStatus.APPLIED);
                job.setAppliedDate(LocalDate.of(2026, 9, 10));
                job.setJobUrl("https://amazon.jobs");
                job.setCreatedAt(LocalDateTime.now());
                job.setUpdatedAt(LocalDateTime.now());

                when(jobRepository.findById(1L))
                                .thenReturn(Optional.of(job));

                JobResponse response = jobService.getJobById(1L);

                assertEquals(1L, response.getId());
                assertEquals("Amazon", response.getCompany());
                assertEquals("Software Engineer", response.getPosition());
                assertEquals(JobStatus.APPLIED, response.getStatus());
        }

        @Test
        void getJobById_shouldThrowExceptionWhenJobNotFound() {

                when(jobRepository.findById(99L))
                                .thenReturn(Optional.empty());

                assertThrows(
                                JobNotFoundException.class,
                                () -> jobService.getJobById(99L));
        }

        @Test
        void createJob_shouldSaveAndReturnJobResponse() {

                JobRequest request = new JobRequest();

                request.setCompany("Google");
                request.setPosition("Software Engineer");
                request.setLocation("Austin, TX");
                request.setStatus(JobStatus.APPLIED);
                request.setAppliedDate(LocalDate.of(2026, 9, 11));
                request.setJobUrl("https://careers.google.com");

                Job savedJob = new Job();

                savedJob.setId(10L);
                savedJob.setCompany("Google");
                savedJob.setPosition("Software Engineer");
                savedJob.setLocation("Austin, TX");
                savedJob.setStatus(JobStatus.APPLIED);
                savedJob.setAppliedDate(LocalDate.of(2026, 9, 11));
                savedJob.setJobUrl("https://careers.google.com");
                savedJob.setCreatedAt(LocalDateTime.now());
                savedJob.setUpdatedAt(LocalDateTime.now());

                when(jobRepository.save(org.mockito.ArgumentMatchers.any(Job.class)))
                                .thenReturn(savedJob);

                JobResponse response = jobService.createJob(request);

                assertEquals(10L, response.getId());
                assertEquals("Google", response.getCompany());
                assertEquals("Software Engineer", response.getPosition());
                assertEquals(JobStatus.APPLIED, response.getStatus());
        }

        @Test
        void updateJob_shouldUpdateAndReturnJobResponse() {

                Job existingJob = new Job();
                existingJob.setId(1L);
                existingJob.setCompany("Amazon");
                existingJob.setPosition("Software Engineer");
                existingJob.setLocation("Dallas, TX");
                existingJob.setStatus(JobStatus.APPLIED);
                existingJob.setAppliedDate(LocalDate.of(2026, 9, 1));
                existingJob.setJobUrl("https://amazon.jobs");
                existingJob.setCreatedAt(LocalDateTime.now().minusDays(5));
                existingJob.setUpdatedAt(LocalDateTime.now().minusDays(5));

                JobRequest request = new JobRequest();
                request.setCompany("Amazon");
                request.setPosition("Software Engineer II");
                request.setLocation("Austin, TX");
                request.setStatus(JobStatus.INTERVIEW);
                request.setAppliedDate(LocalDate.of(2026, 9, 1));
                request.setJobUrl("https://amazon.jobs");

                when(jobRepository.findById(1L))
                                .thenReturn(Optional.of(existingJob));

                when(jobRepository.save(existingJob))
                                .thenReturn(existingJob);

                JobResponse response = jobService.updateJob(1L, request);

                assertEquals(1L, response.getId());
                assertEquals("Amazon", response.getCompany());
                assertEquals("Software Engineer II", response.getPosition());
                assertEquals("Austin, TX", response.getLocation());
                assertEquals(JobStatus.INTERVIEW, response.getStatus());
        }

        @Test
        void deleteJob_shouldDeleteJobWhenJobExists() {

                when(jobRepository.existsById(1L))
                                .thenReturn(true);

                jobService.deleteJob(1L);

                org.mockito.Mockito.verify(jobRepository)
                                .deleteById(1L);
        }

        @Test
        void deleteJob_shouldThrowExceptionWhenJobDoesNotExist() {

                when(jobRepository.existsById(99L))
                                .thenReturn(false);

                assertThrows(
                                JobNotFoundException.class,
                                () -> jobService.deleteJob(99L));

                org.mockito.Mockito.verify(
                                jobRepository,
                                org.mockito.Mockito.never()).deleteById(99L);
        }

}