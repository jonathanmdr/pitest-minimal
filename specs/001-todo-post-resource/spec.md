# Feature Specification: TODO POST Resource

**Feature Branch**: `001-todo-post-resource`  
**Created**: 2026-02-10  
**Status**: Draft  
**Input**: User description: "Create a simple project using Java 25 and Spring Boot 4.0.x containing only a POST resource to save a TODO using an in-memory database, with Unit and Integration tests tagged accordingly, and PITest mutation analysis configured via Gradle (Groovy syntax)."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Create a TODO item via POST endpoint (Priority: P1)

As a client of the TODO API, I want to send a POST request with a TODO payload so that a new TODO item is persisted in the in-memory database and the created item is returned.

**Why this priority**: This is the single core feature of the application. Without the ability to create TODOs, there is no product.

**Independent Test**: Can be fully tested by sending a POST request to `/api/todos` with a JSON body containing `title` and optionally `description` / `completed`, and verifying the response contains the created TODO with an auto-generated `id` and HTTP 201 status.

**Acceptance Scenarios**:

1. **Given** the API is running and no TODOs exist, **When** a client sends `POST /api/todos` with body `{"title": "Buy groceries"}`, **Then** the response status is `201 Created`, the body contains the TODO with a generated `id`, `title` = "Buy groceries", `completed` = false, and the TODO is persisted in the database.

2. **Given** the API is running, **When** a client sends `POST /api/todos` with body `{"title": "Read a book", "description": "Finish chapter 5", "completed": false}`, **Then** the response status is `201 Created` and the body contains all provided fields plus a generated `id`.

3. **Given** the API is running, **When** a client sends `POST /api/todos` with an empty body or missing `title`, **Then** the response status is `400 Bad Request` with a structured error body containing field-level error details (e.g., `{"errors": [{"field": "title", "message": "must not be blank"}]}`).

4. **Given** the API is running, **When** a client sends `POST /api/todos` with a blank `title` (empty string or only whitespace), **Then** the response status is `400 Bad Request` with a structured error body indicating the title is required.

---

### Edge Cases

- What happens when the request body is malformed JSON? → 400 Bad Request
- What happens when `title` exceeds 255 characters? → 400 Bad Request with validation error
- What happens when extra unknown fields are sent in the body? → Unknown fields are ignored; TODO is created with known fields only
- What happens when Content-Type is not `application/json`? → 415 Unsupported Media Type

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST expose a `POST /api/todos` endpoint that accepts JSON payloads
- **FR-002**: System MUST validate that `title` is present and not blank (max 255 characters)
- **FR-003**: System MUST auto-generate a unique `id` for each created TODO
- **FR-004**: System MUST default `completed` to `false` if not provided
- **FR-005**: System MUST persist created TODOs in an H2 in-memory database
- **FR-006**: System MUST return `201 Created` with the full TODO entity in the response body
- **FR-007**: System MUST return `400 Bad Request` with a custom structured error body (e.g., `{"errors": [{"field": "title", "message": "must not be blank"}]}`) for invalid input
- **FR-008**: Unit tests MUST be tagged with `@Tag("unit")` and cover the service layer
- **FR-009**: Integration tests MUST be tagged with `@Tag("integration")` and cover the controller/HTTP layer (full request lifecycle)
- **FR-010**: PITest mutation testing MUST be configured via Gradle plugin with Groovy syntax, running against unit tests only (`@Tag("unit")`)
- **FR-011**: Mutation coverage MUST meet the constitutional minimum of ≥ 75%
- **FR-012**: Line coverage MUST meet the constitutional minimum of ≥ 80%
- **FR-013**: Base package MUST be `com.pitminimal.todo`
- **FR-014**: ID generation MUST use `@GeneratedValue(strategy = GenerationType.IDENTITY)` (auto-increment)

### Key Entities

- **Todo**: Represents a task item. Attributes: `id` (Long, auto-generated), `title` (String, required, max 255), `description` (String, optional), `completed` (Boolean, default false)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: `POST /api/todos` with valid payload returns 201 and the persisted TODO with an `id` within 100ms
- **SC-002**: `POST /api/todos` with invalid payload returns 400 with clear error messages within 100ms
- **SC-003**: All unit tests pass and are tagged with `@Tag("unit")`
- **SC-004**: All integration tests pass and are tagged with `@Tag("integration")`
- **SC-005**: PITest mutation coverage ≥ 75%
- **SC-006**: Line coverage ≥ 80%
- **SC-007**: Gradle build completes within 2 minutes including all checks
