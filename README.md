# Job Tracker API

A RESTful backend application built with Java and Spring Boot to manage job applications. It supports creating, retrieving, updating, deleting, filtering, sorting, and paginating job application records.

## Features

- **CRUD operations:** Create, retrieve, update, and delete job applications.
- **Filtering:** Search by application status, company name, and location.
- **Pagination and sorting:** Retrieve records in pages and sort by supported fields.
- **Request validation:** Validate incoming requests and reject invalid data.
- **Centralized exception handling:** Consistent error responses for validation failures, invalid parameters, and missing records.
- **Database migrations:** Manage database schema changes using Flyway.
- **Logging:** Application activity and relevant events logged using SLF4J.
- **Health monitoring:** Spring Boot Actuator health endpoint.
- **API documentation:** Interactive Swagger UI documentation.
- **Automated testing:** Unit tests, controller tests, and PostgreSQL integration tests.

## Technology Stack

| Category            | Technologies                              |
| ------------------- | ----------------------------------------- |
| Language            | Java 21                                   |
| Framework           | Spring Boot 4.1.1                         |
| Data access         | Spring Data JPA, Hibernate                |
| Database            | PostgreSQL 18                             |
| Database migrations | Flyway                                    |
| API documentation   | Springdoc OpenAPI / Swagger UI            |
| Testing             | JUnit 5, Mockito, MockMvc, Testcontainers |
| Build tool          | Maven                                     |
| Containerization    | Docker, Docker Compose                    |
| Monitoring          | Spring Boot Actuator                      |

## Project Structure

```text
job-tracker-api/
├── src/
│   ├── main/
│   │   ├── java/com/likhitha/job_tracker_api/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── specification/
│   │   └── resources/
│   │       ├── db/migration/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-docker.properties
│   └── test/
├── Dockerfile
├── compose.yaml
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 21
- Maven (or use the included Maven Wrapper)
- PostgreSQL 18, or Docker Desktop with Docker Compose

### Run with Docker Compose

1. Clone the repository:

   ```bash
   git clone YOUR_GITHUB_REPOSITORY_URL
   cd job-tracker-api
   ```

2. Set the database password in your terminal.

   PowerShell:

   ```powershell
   $env:DB_PASSWORD = "your-database-password"
   ```

3. Build and start the application:

   ```powershell
   docker compose up --build -d
   ```

4. Check the containers:

   ```powershell
   docker compose ps
   ```

The API will be available at:

```text
http://localhost:8080
```

**Note:** If you already have a PostgreSQL Docker volume initialized, use the password associated with that existing database volume. Changing the environment variable alone does not change the database user's existing password.

### Run Locally

1. Make sure PostgreSQL is running and a database named `job_tracker` exists.
2. Set the database password:

   ```powershell
   $env:DB_PASSWORD = "your-local-postgres-password"
   ```

3. Start the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The default development profile connects to PostgreSQL at:

```text
jdbc:postgresql://localhost:5432/job_tracker
```

## API Endpoints

Base URL: `http://localhost:8080/api/jobs`

| Method | Endpoint         | Description                                                    |
| ------ | ---------------- | -------------------------------------------------------------- |
| GET    | `/api/jobs`      | Retrieve jobs with optional filtering, sorting, and pagination |
| GET    | `/api/jobs/{id}` | Retrieve a job by ID                                           |
| POST   | `/api/jobs`      | Create a job application                                       |
| PUT    | `/api/jobs/{id}` | Update an existing job application                             |
| DELETE | `/api/jobs/{id}` | Delete a job application                                       |

### Example: Create a Job

**Request**

```http
POST /api/jobs
Content-Type: application/json
```

```json
{
  "company": "Amazon",
  "position": "Software Engineer",
  "location": "Austin, TX",
  "status": "APPLIED",
  "appliedDate": "2026-11-08",
  "jobUrl": "https://www.amazon.jobs"
}
```

### Example: Filter and Paginate Jobs

```http
GET /api/jobs?status=APPLIED&company=Amazon&page=0&size=5&sort=appliedDate,desc
```

Supported job statuses:

- `APPLIED`
- `INTERVIEW`
- `OFFER`
- `REJECTED`

## API Documentation and Monitoring

Once the application is running:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **Health endpoint:** http://localhost:8080/actuator/health

## Running Tests

Run the complete test suite:

```powershell
.\mvnw.cmd test
```

Package the application:

```powershell
.\mvnw.cmd package
```

The project currently has **30 automated tests passing**, including controller tests, service tests, PostgreSQL integration tests using Testcontainers, and an application context test.

## Docker Commands

Check running containers:

```powershell
docker compose ps
```

View application logs:

```powershell
docker compose logs app
```

View PostgreSQL logs:

```powershell
docker compose logs postgres
```

Stop the application and database containers without deleting the database volume:

```powershell
docker compose down
```

## Future Improvements

- Add authentication and authorization.
- Add automated API documentation examples.
- Deploy the application to a cloud platform.
- Add CI/CD automation.
