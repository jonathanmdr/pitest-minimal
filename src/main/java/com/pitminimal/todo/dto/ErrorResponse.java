package com.pitminimal.todo.dto;

import java.util.List;

public class ErrorResponse {

    private List<FieldError> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public record FieldError(String field, String message) {
    }
}
