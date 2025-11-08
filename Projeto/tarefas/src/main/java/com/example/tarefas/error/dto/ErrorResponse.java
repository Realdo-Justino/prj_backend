package com.example.tarefas.error.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {
    @Schema(example = "Task not found")
    public String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
