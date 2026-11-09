package com.likhitha.job_tracker_api.controller;

import com.likhitha.job_tracker_api.dto.JobResponse;
import com.likhitha.job_tracker_api.model.JobStatus;
import com.likhitha.job_tracker_api.service.JobService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.likhitha.job_tracker_api.exception.JobNotFoundException;

import tools.jackson.databind.json.JsonMapper;
import com.likhitha.job_tracker_api.dto.JobRequest;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@WebMvcTest(JobController.class)
class JobControllerTest {

        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private JsonMapper jsonMapper;

        @MockitoBean
        private JobService jobService;

        @Test
        void getJobById_shouldReturnJob() throws Exception {

                JobResponse response = new JobResponse();

                response.setId(1L);
                response.setCompany("Amazon");
                response.setPosition("Software Engineer");
                response.setLocation("Dallas, TX");
                response.setStatus(JobStatus.APPLIED);
                response.setAppliedDate(LocalDate.of(2026, 9, 11));
                response.setJobUrl("https://amazon.jobs");
                response.setCreatedAt(LocalDateTime.now());
                response.setUpdatedAt(LocalDateTime.now());

                when(jobService.getJobById(1L))
                                .thenReturn(response);

                mockMvc.perform(get("/api/jobs/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.company").value("Amazon"))
                                .andExpect(jsonPath("$.position").value("Software Engineer"))
                                .andExpect(jsonPath("$.status").value("APPLIED"));
        }

        @Test
        void getJobById_shouldReturn404WhenJobNotFound() throws Exception {

                when(jobService.getJobById(99L))
                                .thenThrow(new JobNotFoundException(99L));

                mockMvc.perform(get("/api/jobs/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void createJob_shouldReturn201AndCreatedJob() throws Exception {

                JobRequest request = new JobRequest();
                request.setCompany("Google");
                request.setPosition("Software Engineer");
                request.setLocation("Austin, TX");
                request.setStatus(JobStatus.APPLIED);
                request.setAppliedDate(LocalDate.of(2026, 9, 11));
                request.setJobUrl("https://careers.google.com");

                JobResponse response = new JobResponse();
                response.setId(10L);
                response.setCompany("Google");
                response.setPosition("Software Engineer");
                response.setLocation("Austin, TX");
                response.setStatus(JobStatus.APPLIED);
                response.setAppliedDate(LocalDate.of(2026, 9, 11));
                response.setJobUrl("https://careers.google.com");
                response.setCreatedAt(LocalDateTime.now());
                response.setUpdatedAt(LocalDateTime.now());

                when(jobService.createJob(org.mockito.ArgumentMatchers.any(JobRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(
                                post("/api/jobs")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonMapper.writeValueAsString(request)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(10))
                                .andExpect(jsonPath("$.company").value("Google"))
                                .andExpect(jsonPath("$.position").value("Software Engineer"))
                                .andExpect(jsonPath("$.status").value("APPLIED"));
        }

        @Test
        void createJob_shouldReturn400WhenRequestIsInvalid() throws Exception {

                JobRequest request = new JobRequest();

                request.setCompany("");
                request.setPosition("");
                request.setLocation("");
                request.setStatus(null);
                request.setAppliedDate(LocalDate.of(2026, 9, 11));
                request.setJobUrl("https://example.com");

                mockMvc.perform(
                                post("/api/jobs")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());

                verify(jobService, never())
                                .createJob(org.mockito.ArgumentMatchers.any(JobRequest.class));
        }

        @Test
        void updateJob_shouldReturn200AndUpdatedJob() throws Exception {

                JobRequest request = new JobRequest();
                request.setCompany("Amazon");
                request.setPosition("Software Engineer II");
                request.setLocation("Austin, TX");
                request.setStatus(JobStatus.INTERVIEW);
                request.setAppliedDate(LocalDate.of(2026, 9, 11));
                request.setJobUrl("https://amazon.jobs");

                JobResponse response = new JobResponse();
                response.setId(1L);
                response.setCompany("Amazon");
                response.setPosition("Software Engineer II");
                response.setLocation("Austin, TX");
                response.setStatus(JobStatus.INTERVIEW);
                response.setAppliedDate(LocalDate.of(2026, 9, 11));
                response.setJobUrl("https://amazon.jobs");
                response.setCreatedAt(LocalDateTime.now().minusDays(5));
                response.setUpdatedAt(LocalDateTime.now());

                when(
                                jobService.updateJob(
                                                org.mockito.ArgumentMatchers.eq(1L),
                                                org.mockito.ArgumentMatchers.any(JobRequest.class)))
                                .thenReturn(response);

                mockMvc.perform(
                                put("/api/jobs/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(jsonMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.company").value("Amazon"))
                                .andExpect(jsonPath("$.position").value("Software Engineer II"))
                                .andExpect(jsonPath("$.location").value("Austin, TX"))
                                .andExpect(jsonPath("$.status").value("INTERVIEW"));
        }

        @Test
        void deleteJob_shouldReturn204WhenJobExists() throws Exception {

                mockMvc.perform(delete("/api/jobs/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        void deleteJob_shouldReturn404WhenJobDoesNotExist() throws Exception {

                org.mockito.Mockito.doThrow(new JobNotFoundException(99L))
                                .when(jobService)
                                .deleteJob(99L);

                mockMvc.perform(delete("/api/jobs/99"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getJobs_shouldReturnFilteredPagedResults() throws Exception {

                JobResponse job = new JobResponse();
                job.setId(1L);
                job.setCompany("Amazon");
                job.setPosition("Software Engineer");
                job.setLocation("Dallas, TX");
                job.setStatus(JobStatus.APPLIED);
                job.setAppliedDate(LocalDate.of(2026, 9, 11));
                job.setJobUrl("https://amazon.jobs");

                Page<JobResponse> page = new PageImpl<>(List.of(job));

                when(
                                jobService.searchJobs(
                                                org.mockito.ArgumentMatchers.eq(JobStatus.APPLIED),
                                                org.mockito.ArgumentMatchers.eq("amazon"),
                                                org.mockito.ArgumentMatchers.eq("dallas"),
                                                org.mockito.ArgumentMatchers.any(Pageable.class)))
                                .thenReturn(page);

                mockMvc.perform(
                                get("/api/jobs")
                                                .param("status", "APPLIED")
                                                .param("company", "amazon")
                                                .param("location", "dallas")
                                                .param("page", "0")
                                                .param("size", "5"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].company").value("Amazon"))
                                .andExpect(jsonPath("$.content[0].location").value("Dallas, TX"))
                                .andExpect(jsonPath("$.content[0].status").value("APPLIED"));
        }

        @Test
        void getJobs_shouldReturn400WhenPageIsNegative() throws Exception {

                mockMvc.perform(
                                get("/api/jobs")
                                                .param("page", "-1"))
                                .andExpect(status().isBadRequest());

                verify(jobService, never()).searchJobs(
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        void getJobs_shouldReturn400WhenPageSizeIsZero() throws Exception {

                mockMvc.perform(
                                get("/api/jobs")
                                                .param("size", "0"))
                                .andExpect(status().isBadRequest());

                verify(jobService, never()).searchJobs(
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        void getJobs_shouldReturn400WhenSortFieldIsInvalid() throws Exception {

                mockMvc.perform(
                                get("/api/jobs")
                                                .param("sort", "banana"))
                                .andExpect(status().isBadRequest());

                verify(jobService, never()).searchJobs(
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        void getJobs_shouldReturn400WhenStatusIsInvalid() throws Exception {

                mockMvc.perform(
                                get("/api/jobs")
                                                .param("status", "PENDING"))
                                .andExpect(status().isBadRequest());

                verify(jobService, never()).searchJobs(
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(),
                                org.mockito.ArgumentMatchers.any(Pageable.class));
        }

        @Test
        void createJob_shouldReturn400WhenDateFormatIsInvalid() throws Exception {

                String invalidRequest = """
                                {
                                    "company": "Google",
                                    "position": "Software Engineer",
                                    "location": "Austin, TX",
                                    "status": "APPLIED",
                                    "appliedDate": "2026/09/11",
                                    "jobUrl": "https://careers.google.com"
                                }
                                """;

                mockMvc.perform(
                                post("/api/jobs")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(invalidRequest))
                                .andExpect(status().isBadRequest());

                verify(jobService, never())
                                .createJob(org.mockito.ArgumentMatchers.any(JobRequest.class));
        }

}
