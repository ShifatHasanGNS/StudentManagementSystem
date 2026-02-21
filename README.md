# StudentManagementSystem

> A Students, Teachers and Course Management WebApp built using Java 25 and Spring Boot 4.

## Tech Stack

- **Java 25** with Spring Boot 4.0.2
- **Spring MVC** + Thymeleaf (server-side rendering)
- **Spring Security** with role-based access control (STUDENT / TEACHER)
- **Spring Data JPA** + PostgreSQL (production)
- **H2** in-memory database (testing)
- **Docker** + Docker Compose for containerised deployment

## Dependencies

| Dependency           | Purpose                             |
| -------------------- | ----------------------------------- |
| Spring Web           | MVC controllers                     |
| Spring Security      | Authentication & authorisation      |
| Spring Data JPA      | ORM / persistence                   |
| Thymeleaf            | Server-side HTML templating         |
| PostgreSQL Driver    | Production database                 |
| H2 Database          | In-memory DB for tests              |
| Spring Boot DevTools | Live reload in development          |
| Spring Boot Test     | Unit & integration testing          |
| Spring Security Test | MockMvc security helpers            |
| Testcontainers       | Container-based integration testing |

## Getting Started

### With Docker (recommended)

```bash
docker-compose up --build
```

The app will be available at `http://localhost:8080`.

Default credentials seeded on first run:

| Role            | Username         | Password   |
| --------------- | ---------------- | ---------- |
| Student         | student@test.com | student123 |
| Teacher         | teacher@test.com | teacher123 |
| Admin (Teacher) | admin@test.com   | admin123   |

### Local Development (without Docker)

1. Start a PostgreSQL instance and update `src/main/resources/application-dev.properties` with your credentials.
2. Run the app:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Running Tests

```bash
./mvnw test
```

Tests use an H2 in-memory database and do not require PostgreSQL or Docker to be running. The test suite includes:

- **Unit tests** — service layer (Mockito)
- **Integration tests** — controllers (MockMvc + Spring Security) and repository (JPA / H2)

## Project Structure

```
src/
├── main/
│   ├── java/.../studentmanagementsystem/
│   │   ├── controller/     # MVC controllers
│   │   ├── model/          # JPA entities (Student, Teacher, Course, Department)
│   │   ├── repository/     # ManagementRepository (EntityManager-based)
│   │   ├── security/       # SecurityConfig, UserAccount, UserDetailsServiceImpl
│   │   ├── service/        # Business logic
│   │   └── DataLoader.java # Seeds default data on startup (skipped in tests)
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── templates/      # Thymeleaf HTML templates
└── test/
    ├── java/.../
    │   ├── controller/     # Integration tests (MockMvc)
    │   ├── repository/     # Integration tests (H2 + JPA)
    │   └── service/        # Unit tests (Mockito)
    └── resources/
        └── application-test.properties  # H2 config for test profile
```

## Branches

- `main` _(protected)_
- `feature`
