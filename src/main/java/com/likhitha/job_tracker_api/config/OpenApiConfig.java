
package com.likhitha.job_tracker_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobTrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Job Tracker API")
                        .version("1.0.0")
                        .description(
                                "REST API for managing and tracking job applications. "
                                        + "Supports creating, retrieving, updating, deleting, "
                                        + "filtering, sorting, and paginating job applications.")
                        .contact(new Contact()
                                .name("API Support")));
    }
}
