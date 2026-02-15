# Task Manager API

A RESTful API for managing tasks built with Spring Boot and PostgreSQL.

##  Technologies

- **Java 20**
- **Spring Boot 3.x**
- **Spring Data JPA / Hibernate**
- **PostgreSQL 15**
- **Maven**
- **Docker**
- **Swagger/OpenAPI** - API Documentation

##  Features

- ✅ CRUD operations for tasks
- ✅ PostgreSQL database integration
- ✅ Custom JPA queries
- ✅ Interactive API documentation with Swagger
- ✅ Dockerized application
- ✅ RESTful API design

##  Prerequisites

- Java 20+
- Docker Desktop
- Maven (or use included wrapper)

##  Quick Start

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

### 2. Run the Application
```bash
./mvnw spring-boot:run
```

Or on Windows:
```bash
mvnw.cmd spring-boot:run
```

### 3. Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

##  Configuration

The application uses environment variables for database configuration.

Default values (for local development):
- `DB_NAME`: taskdb
- `DB_USERNAME`: admin
- `DB_PASSWORD`: admin123

For production, override these with actual environment variables.

See `application.properties.example` for reference.

##  API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}` | Update task |
| PATCH | `/api/tasks/{id}` | Partial update |
| DELETE | `/api/tasks/{id}` | Delete task |
| GET | `/api/tasks/incomplete` | Get incomplete tasks |
| GET | `/api/tasks/search/title?title=...` | Search by title |
| GET | `/api/tasks/count/completed` | Count completed tasks |

Full API documentation available at `/swagger-ui.html` when running.

##  Docker

Build and run with Docker:
```bash
# Build the JAR
./mvnw clean package

# Build Docker image
docker build -t task-manager:1.0 .

# Run
docker run -d -p 8080:8080 --name task-app task-manager:1.0
```

##  Learning Project

This project is part of my Backend Development learning journey, covering:
- Spring Boot & Maven
- JPA & Hibernate
- PostgreSQL
- Docker
- REST API design
- Swagger documentation

##  License

This is a learning project - feel free to use and modify as needed.