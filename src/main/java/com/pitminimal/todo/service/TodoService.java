package com.pitminimal.todo.service;

import com.pitminimal.todo.dto.CreateTodoRequest;
import com.pitminimal.todo.dto.TodoResponse;
import com.pitminimal.todo.entity.Todo;
import com.pitminimal.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoResponse createTodo(CreateTodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);

        Todo savedTodo = todoRepository.save(todo);

        return new TodoResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getDescription(),
                savedTodo.getCompleted()
        );
    }
}
