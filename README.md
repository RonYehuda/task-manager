# Task Manager API

A RESTful API for managing tasks and users, built with Spring Boot and PostgreSQL. This is a monolithic backend application covering a wide range of real-world backend concepts.

## Technologies

- **Java 17**
- **Spring Boot 4.x**
- **Spring Security** - JWT-based authentication and authorization
- **Spring Data JPA / Hibernate** - ORM and database access
- **PostgreSQL 15** - Relational database
- **AWS S3** - File storage with presigned URLs
- **Maven** - Build and dependency management
- **Docker & Docker Compose** - Containerization
- **Swagger / OpenAPI** - Interactive API documentation

## Features

- JWT authentication (register, login, protected routes)
- Full CRUD for users and tasks
- JPA relationships (User has many Tasks)
- Custom JPA queries (search, count, filter)
- Dynamic filtering, pagination, and sorting
- File upload and download via AWS S3 with presigned URLs
- Async bulk task import with a dedicated thread pool
- Async email notification on task creation
- Docker Compose setup for full local environment (app + PostgreSQL)

## Prerequisites

- Java 17+
- Docker Desktop
- Maven (or use the included wrapper)
- AWS account with an S3 bucket and IAM user credentials

## Quick Start

### 1. Configure environment variables

Copy `.env.example` to `.env` and fill in your values:

```
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_KEY=your-secret-key
JWT_SECRET=your-base64-encoded-secret
```

### 2. Start with Docker Compose

```bash
docker-compose up -d --build
```

This starts both the PostgreSQL database and the Spring Boot application.

### 3. Access Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Running Locally Without Docker

### 1. Start PostgreSQL

```bash
docker run -d \
  --name postgres-db \
  -e POSTGRES_DB=taskdb \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  postgres:15
```

### 2. Update application.properties

Change the datasource URL from `postgres` (Docker Compose hostname) to `localhost`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

## Configuration

Environment variables used by the application:

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_NAME` | PostgreSQL database name | `taskdb` |
| `DB_USERNAME` | PostgreSQL username | `admin` |
| `DB_PASSWORD` | PostgreSQL password | `admin123` |
| `AWS_ACCESS_KEY_ID` | AWS access key | required |
| `AWS_SECRET_KEY` | AWS secret key | required |
| `JWT_SECRET` | Base64-encoded JWT signing secret | required |

## API Endpoints

### Authentication

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register a new user | No |
| POST | `/api/auth/login` | Login and receive JWT token | No |

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create a user |
| PUT | `/api/users/{id}` | Update a user |
| DELETE | `/api/users/{id}` | Delete a user |
| GET | `/api/users/order` | Get all users ordered by username |

### Tasks

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks (paginated) |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| PATCH | `/api/tasks/{id}/complete` | Mark task as completed |
| GET | `/api/tasks/incomplete` | Get incomplete tasks |
| GET | `/api/tasks/search/title` | Search tasks by title |
| GET | `/api/tasks/search/description` | Search tasks by description |
| GET | `/api/tasks/count/completed` | Count completed tasks |
| GET | `/api/tasks/count/incomplete` | Count incomplete tasks |
| GET | `/api/tasks/ordered` | Get all tasks ordered by title |
| GET | `/api/tasks/filter` | Filter tasks by status, user, title (paginated) |
| GET | `/api/tasks/filter/date` | Filter tasks by creation date (paginated) |
| POST | `/api/tasks/bulk-import` | Async bulk import of tasks |

### Attachments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks/{taskId}/attachments` | Get all attachments for a task |
| POST | `/api/tasks/{taskId}/attachments` | Upload a file attachment |
| GET | `/api/attachments/{id}/download` | Get a presigned download URL |
| DELETE | `/api/attachments/{id}` | Delete an attachment |

## Docker

Build and run manually with Docker:

```bash
# Build the JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t task-manager:1.0 .

# Run (requires a running PostgreSQL instance and environment variables)
docker run -d \
  -p 8080:8080 \
  --name task-app \
  -e DB_NAME=taskdb \
  -e DB_USERNAME=admin \
  -e DB_PASSWORD=admin123 \
  -e AWS_ACCESS_KEY_ID=your-key \
  -e AWS_SECRET_KEY=your-secret \
  -e JWT_SECRET=your-secret \
  task-manager:1.0
```

## Project Structure

```
src/main/java/com/ron/taskmanager/
├── config/          # Security, Swagger, Async configuration
├── controller/      # REST controllers
├── filter/          # JWT authentication filter
├── model/           # JPA entities (User, Task, TaskAttachment)
├── repository/      # Spring Data JPA repositories
└── service/         # Business logic (TaskService, UserService, S3Service, JwtService...)
```

## Authentication Flow

All endpoints except `/api/auth/**` and `/swagger-ui/**` require a valid JWT token.

1. Register a user via `POST /api/auth/register`
2. Login via `POST /api/auth/login` to receive a token
3. Include the token in subsequent requests: `Authorization: Bearer <token>`