# Implementation Plan: TODO POST Resource

**Branch**: `001-todo-post-resource` | **Date**: 2026-02-10 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-todo-post-resource/spec.md`

## Summary

Build a minimal Spring Boot REST API with a single `POST /api/todos` endpoint that creates TODO items in an H2 in-memory database. The project uses Java 25, Gradle 9.x with Groovy syntax, and is configured with PITest for mutation testing (scoped to unit tests only). Tests are organized with JUnit 5 `@Tag` annotations: `"unit"` for unit tests and `"integration"` for integration tests. Validation errors use a custom structured error body with field-level details.

## Technical Context

**Language/Version**: Java 25  
**Primary Dependencies**: Spring Boot 4.0.x (spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-validation)  
**Storage**: H2 in-memory database (spring-boot-starter-data-jpa + H2 runtime dependency)  
**Testing**: JUnit 5 with `@Tag` annotations, Mockito, Spring Boot Test (`@SpringBootTest`, `MockMvc`)  
**Mutation Testing**: PITest via `info.solidsoft.pitest` Gradle plugin (scoped to unit tests only via `@Tag("unit")`)  
**Build Tool**: Gradle 9.x (latest GA stable) with Groovy DSL (`build.gradle`)  
**Target Platform**: JVM (local development / CI)  
**Performance Goals**: Individual unit tests < 100ms, integration tests < 1s, full build < 2 minutes  
**Constraints**: Mutation coverage ≥ 75%, Line coverage ≥ 80% (per constitution)  
**Scale/Scope**: Single endpoint, single entity, minimal project  
**Base Package**: `com.pitminimal.todo`  
**ID Strategy**: `@GeneratedValue(strategy = GenerationType.IDENTITY)` — auto-increment  
**Error Response**: Custom structured body — `{"errors": [{"field": "...", "message": "..."}]}`

## Constitution Check

| Gate | Status | Notes |
|------|--------|-------|
| Test Quality (Given/When/Then) | ✅ Will comply | All tests structured with Given/When/Then |
| Mutation Coverage ≥ 75% | ✅ Will comply | PITest configured with threshold, scoped to unit tests only |
| Line Coverage ≥ 80% | ✅ Will comply | JaCoCo configured with threshold |
| Method ≤ 30 lines | ✅ Will comply | Simple controller/service methods |
| Cyclomatic complexity ≤ 10 | ✅ Will comply | Minimal branching |
| Single responsibility | ✅ Will comply | Controller → Service → Repository layers |
| Build time ≤ 2 minutes | ✅ Will comply | Minimal project scope |
| Unit tests < 100ms | ✅ Will comply | Mocked dependencies |
| Integration tests < 1s | ✅ Will comply | In-memory DB, embedded server |

## Project Structure

### Documentation (this feature)

```text
specs/001-todo-post-resource/
├── spec.md              # Feature specification
├── plan.md              # This file
└── tasks.md             # Task breakdown
```

### Source Code (repository root)

```text
├── build.gradle                          # Gradle build with Groovy DSL
├── settings.gradle                       # Project settings
├── gradle.properties                     # Gradle properties
├── gradlew / gradlew.bat                 # Gradle wrapper
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/pitminimal/todo/
    │   │       ├── TodoApplication.java           # Spring Boot main class
    │   │       ├── controller/
    │   │       │   └── TodoController.java        # REST controller (POST endpoint)
    │   │       ├── dto/
    │   │       │   ├── CreateTodoRequest.java      # Request DTO with validation
    │   │       │   ├── TodoResponse.java           # Response DTO
    │   │       │   └── ErrorResponse.java          # Custom error response DTO
    │   │       ├── entity/
    │   │       │   └── Todo.java                   # JPA entity
    │   │       ├── exception/
    │   │       │   └── GlobalExceptionHandler.java  # @ControllerAdvice for validation errors
    │   │       ├── repository/
    │   │       │   └── TodoRepository.java         # Spring Data JPA repository
    │   │       └── service/
    │   │           └── TodoService.java            # Business logic
    │   └── resources/
    │       └── application.properties              # H2, JPA config
    └── test/
        └── java/
            └── com/pitminimal/todo/
                ├── service/
                │   └── TodoServiceTest.java        # Unit tests (@Tag("unit"))
                └── integration/
                    └── TodoApiIntegrationTest.java  # Integration tests (@Tag("integration"))
```

**Structure Decision**: Single-project structure following standard Spring Boot / Gradle conventions. Production code under `src/main/java`, tests under `src/test/java` mirroring the package structure. Unit and integration tests are co-located under `src/test/java` but distinguished by `@Tag` annotations and Gradle test task configuration.

## Complexity Tracking

No violations — the project is minimal and well within all constitutional limits.
