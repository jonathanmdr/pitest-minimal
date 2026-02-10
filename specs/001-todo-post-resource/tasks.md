# Tasks: TODO POST Resource

**Input**: Design documents from `/specs/001-todo-post-resource/`
**Prerequisites**: plan.md (required), spec.md (required for user stories)

**Tests**: Tests are explicitly requested. Unit tests tagged with `@Tag("unit")`, integration tests tagged with `@Tag("integration")`. Tests follow Given/When/Then structure per constitution.

**Organization**: Single user story (P1) — all tasks support the POST /api/todos endpoint.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization, Gradle configuration, and dependency setup

- [ ] T001 Initialize Gradle 9.x wrapper and project settings in `settings.gradle`
- [ ] T002 Configure `build.gradle` with Spring Boot 4.0.x, Java 25, H2, JPA, Validation, JUnit 5, Mockito, PITest plugin (scoped to `@Tag("unit")` tests only), JaCoCo, and a single `test` task that runs all tags
- [ ] T003 [P] Create `gradle.properties` with JVM and project metadata
- [ ] T004 [P] Create `application.properties` with H2 in-memory database and JPA configuration in `src/main/resources/application.properties`

**Checkpoint**: Project compiles with `./gradlew build` (no source code yet, but configuration is valid)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core entity, repository, and Spring Boot application class

**⚠️ CRITICAL**: No user story implementation can begin until this phase is complete

- [ ] T005 Create Spring Boot main class in `src/main/java/com/pitminimal/todo/TodoApplication.java`
- [ ] T006 Create JPA entity `Todo` in `src/main/java/com/pitminimal/todo/entity/Todo.java` with fields: `id` (Long, `@GeneratedValue(strategy = GenerationType.IDENTITY)`), `title` (String, not blank, max 255), `description` (String, optional), `completed` (Boolean, default false)
- [ ] T007 [P] Create Spring Data JPA repository `TodoRepository` in `src/main/java/com/pitminimal/todo/repository/TodoRepository.java`
- [ ] T008 [P] Create request DTO `CreateTodoRequest` in `src/main/java/com/pitminimal/todo/dto/CreateTodoRequest.java` with validation annotations (`@NotBlank`, `@Size`)
- [ ] T009 [P] Create response DTO `TodoResponse` in `src/main/java/com/pitminimal/todo/dto/TodoResponse.java`
- [ ] T009b [P] Create custom error response DTO `ErrorResponse` in `src/main/java/com/pitminimal/todo/dto/ErrorResponse.java` — structured as `{"errors": [{"field": "...", "message": "..."}]}`
- [ ] T009c [P] Create `GlobalExceptionHandler` in `src/main/java/com/pitminimal/todo/exception/GlobalExceptionHandler.java` — `@ControllerAdvice` that catches `MethodArgumentNotValidException` and returns custom `ErrorResponse`

**Checkpoint**: Foundation ready — entity persists in H2, DTOs defined, repository operational

---

## Phase 3: User Story 1 — Create a TODO item via POST (Priority: P1) 🎯 MVP

**Goal**: Implement `POST /api/todos` endpoint that validates input, persists a TODO, and returns the created resource with HTTP 201

**Independent Test**: Send `POST /api/todos` with `{"title": "Buy groceries"}` → receive 201 with generated id

### Tests for User Story 1 ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T010 [P] [US1] Unit test for `TodoService` in `src/test/java/com/pitminimal/todo/service/TodoServiceTest.java` — tagged `@Tag("unit")`, Given/When/Then structure:
  - `shouldCreateTodoWhenValidRequestProvided`
  - `shouldDefaultCompletedToFalseWhenNotProvided`
- [ ] T011 [US1] Integration test for controller in `src/test/java/com/pitminimal/todo/integration/TodoApiIntegrationTest.java` — tagged `@Tag("integration")`, uses `@SpringBootTest` with `TestRestTemplate`:
  - `shouldReturn201WhenValidTodoCreated`
  - `shouldCreateTodoAndPersistInDatabase`
  - `shouldReturn400WhenTitleIsBlank`
  - `shouldReturn400WhenTitleIsMissing`
  - `shouldReturn400WhenTitleExceeds255Characters`
  - `shouldReturnCustomErrorBodyWithFieldDetails`

### Implementation for User Story 1

- [ ] T013 [US1] Implement `TodoService` in `src/main/java/com/pitminimal/todo/service/TodoService.java` — business logic to map DTO → entity, save via repository, return response DTO
- [ ] T014 [US1] Implement `TodoController` in `src/main/java/com/pitminimal/todo/controller/TodoController.java` — `POST /api/todos` with `@Valid` request body, delegates to `TodoService`, returns `ResponseEntity<TodoResponse>` with 201 status
- [ ] T015 [US1] Verify all tests pass: `./gradlew test`
- [ ] T016 [US1] Run PITest mutation analysis: `./gradlew pitest` — verify mutation coverage ≥ 75% (runs against `@Tag("unit")` tests only)
- [ ] T017 [US1] Run JaCoCo coverage report: `./gradlew jacocoTestReport` — verify line coverage ≥ 80%

**Checkpoint**: POST /api/todos is fully functional, all tests pass, mutation and line coverage thresholds met

---

## Phase 4: Polish & Cross-Cutting Concerns

**Purpose**: Final validation and cleanup

- [ ] T018 Run full build: `./gradlew clean build` — verify completes within 2 minutes
- [ ] T019 Review surviving mutants in PITest report and document justification if any are equivalent/unreachable
- [ ] T020 Verify all constitutional gates pass (coverage, build time, test tagging)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — start immediately
- **Foundational (Phase 2)**: Depends on Phase 1 (T001, T002 must be complete)
- **User Story 1 (Phase 3)**: Depends on Phase 2 completion
- **Polish (Phase 4)**: Depends on Phase 3 completion

### Within User Story 1

- Tests (T010, T011) MUST be written FIRST and confirmed to FAIL
- Service (T013) before Controller (T014) — controller depends on service
- All tests must pass (T015) before mutation testing (T016) and coverage (T017)

### Parallel Opportunities

- T003 and T004 can run in parallel (independent config files)
- T007, T008, T009 can run in parallel (independent classes)
- T010 and T011 can run in parallel (independent test files — unit vs integration)
- T013 and T014 are sequential (controller depends on service)
