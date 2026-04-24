# Task Manager API

A production-ready RESTful API for task management, built with Spring Boot and PostgreSQL.

## Features

- **Task CRUD** — Create, read, update, delete, and partial update operations
- **Custom Queries** — Filter incomplete tasks, search by title, count by status
- **PostgreSQL Integration** — Persistent storage with Spring Data JPA / Hibernate
- **Dockerized** — Fully containerized with Docker and docker-compose
- **API Documentation** — Interactive Swagger/OpenAPI docs

## Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

- **Java 20**
- **Spring Boot 3.x** + Spring Data JPA
- **PostgreSQL 15**
- **Maven**
- **Swagger / OpenAPI**

## Getting Started

### Option 1 — Docker Compose (recommended)
```bash
git clone https://github.com/RonYehuda/task-manager.git
cd task-manager
docker-compose up
```

### Option 2 — Run locally
```bash
# Start PostgreSQL
docker run -d \
  --name postgres-db \
  -e POSTGRES_DB=taskdb \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  postgres:15

# Run the app
./mvnw spring-boot:run
```

Open Swagger UI at: `http://localhost:8080/swagger-ui.html`

## API Endpoints

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

## Configuration

The app uses environment variables for database configuration. Copy `.env.example` to `.env` and update as needed.

Default values for local development:
- `DB_NAME`: taskdb
- `DB_USERNAME`: admin
- `DB_PASSWORD`: admin123
