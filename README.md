# kanagat-beken-student-api

Author: **Kanagat Beken**
Project status: initial version of the Student API

Spring Boot CRUD REST API for managing students, with PostgreSQL, Docker, Docker Compose and a Jenkins CI/CD pipeline.
Created for the DevOps midterm.

## Tech stack

- Java 17, Spring Boot 3.3, Spring Data JPA, Bean Validation
- Gradle (wrapper included)
- PostgreSQL 16
- Docker (multi-stage build), Docker Compose
- Jenkins (declarative pipeline in `Jenkinsfile`)

## Project structure

```
kanagat-beken-student-api/
├── scripts/            # Kanagat_Beken_system_report.sh
├── logs/               # Kanagat_Beken_system_report.log
├── backups/            # README backups
├── src/                # Spring Boot source code and tests
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── build.gradle
└── README.md
```

## API

Base URL: `http://localhost:8080/kanagat-beken`

| Method | Path                 | Description            |
|--------|----------------------|------------------------|
| GET    | /api/info            | Author and app version |
| GET    | /api/students        | All students           |
| GET    | /api/students/{id}   | Student by id (404 if not found) |
| POST   | /api/students        | Create student         |
| PUT    | /api/students/{id}   | Update student         |
| DELETE | /api/students/{id}   | Delete student         |

Example body:

```json
{"firstName":"Kanagat","lastName":"Beken","email":"kanagat.beken@example.com","faculty":"Information Technology","course":3}
```

## Run with Docker Compose

```bash
cp .env.example .env          # set your own password in .env
docker compose up -d --build
curl http://localhost:8080/kanagat-beken/api/info
```

## Run tests

```bash
./gradlew test
```

## Security

`.env`, passwords and database credentials are **never** committed. Only `.env.example` with placeholder values is stored in Git.

## Changes by Kanagat Beken

- Added `GET /api/students/search?lastName=...` - search students by last name (case-insensitive, partial match).
- Added test `StudentSearchTest` for the new endpoint.
