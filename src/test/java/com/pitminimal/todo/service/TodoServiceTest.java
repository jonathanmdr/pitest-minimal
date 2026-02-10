package com.pitminimal.todo.service;

import com.pitminimal.todo.dto.CreateTodoRequest;
import com.pitminimal.todo.dto.TodoResponse;
import com.pitminimal.todo.entity.Todo;
import com.pitminimal.todo.repository.TodoRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.argThat;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void shouldCreateTodoWhenValidRequestProvided() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("Buy groceries", "Milk and eggs", false);

        Todo savedTodo = new Todo("Buy groceries", "Milk and eggs", false);
        savedTodo.setId(1L);

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        TodoResponse response = todoService.createTodo(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Buy groceries");
        assertThat(response.getDescription()).isEqualTo("Milk and eggs");
        assertThat(response.getCompleted()).isFalse();
        verify(todoRepository).save(argThat(todo ->
                "Buy groceries".equals(todo.getTitle()) &&
                "Milk and eggs".equals(todo.getDescription()) &&
                Boolean.FALSE.equals(todo.getCompleted())
        ));
    }

    @Test
    void shouldDefaultCompletedToFalseWhenNotProvided() {
        // Given
        CreateTodoRequest request = new CreateTodoRequest("Read a book", null, null);

        Todo savedTodo = new Todo("Read a book", null, false);
        savedTodo.setId(2L);

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        TodoResponse response = todoService.createTodo(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getTitle()).isEqualTo("Read a book");
        assertThat(response.getDescription()).isNull();
        assertThat(response.getCompleted()).isFalse();
        verify(todoRepository).save(argThat(todo ->
                "Read a book".equals(todo.getTitle()) &&
                todo.getDescription() == null &&
                Boolean.FALSE.equals(todo.getCompleted())
        ));
    }
}
