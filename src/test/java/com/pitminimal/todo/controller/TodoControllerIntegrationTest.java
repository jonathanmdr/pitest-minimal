package com.pitminimal.todo.controller;

import com.pitminimal.todo.dto.CreateTodoRequest;
import com.pitminimal.todo.dto.ErrorResponse;
import com.pitminimal.todo.dto.TodoResponse;
import com.pitminimal.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class TodoControllerIntegrationTest {

    private static final String API_TODOS = "/api/todos";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldReturn201WhenValidTodoCreated() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("Buy groceries", null, null);

        // When
        ResponseEntity<TodoResponse> response = restTemplate.postForEntity(
                API_TODOS, request, TodoResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Buy groceries");
        assertThat(response.getBody().getCompleted()).isFalse();
    }

    @Test
    void shouldCreateTodoAndPersistInDatabase() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("Read a book", "Finish chapter 5", false);

        // When
        ResponseEntity<TodoResponse> response = restTemplate.postForEntity(
                API_TODOS, request, TodoResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        Long createdId = response.getBody().getId();
        assertThat(createdId).isNotNull();
        assertThat(todoRepository.findById(createdId)).isPresent();
        assertThat(todoRepository.findById(createdId).get().getTitle()).isEqualTo("Read a book");
        assertThat(todoRepository.findById(createdId).get().getDescription()).isEqualTo("Finish chapter 5");
    }

    @Test
    void shouldReturn400WhenTitleIsBlank() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("   ", null, null);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                API_TODOS, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors())
                .anyMatch(error -> "title".equals(error.field()));
    }

    @Test
    void shouldReturn400WhenTitleIsMissing() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest(null, "Some description", null);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                API_TODOS, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors())
                .anyMatch(error -> "title".equals(error.field()));
    }

    @Test
    void shouldReturn400WhenTitleExceeds255Characters() {
        // Given
        String longTitle = "a".repeat(256);
        CreateTodoRequest request = new CreateTodoRequest(longTitle, null, null);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                API_TODOS, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).isNotEmpty();
        assertThat(response.getBody().getErrors())
                .anyMatch(error -> "title".equals(error.field())
                        && error.message().contains("255"));
    }

    @Test
    void shouldReturnCustomErrorBodyWithFieldDetails() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("", null, null);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                API_TODOS, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getErrors()).isNotEmpty();

        ErrorResponse.FieldError fieldError = response.getBody().getErrors().stream()
                .filter(e -> "title".equals(e.field()))
                .findFirst()
                .orElse(null);

        assertThat(fieldError).isNotNull();
        assertThat(fieldError.field()).isEqualTo("title");
        assertThat(fieldError.message()).isNotBlank();
    }
}
