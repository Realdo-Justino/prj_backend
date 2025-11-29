package com.example.tarefas.exceptions;

public class RefreshTokenNotExistsException extends RuntimeException {
    public RefreshTokenNotExistsException(String message) {
        super(message);
    }
}
