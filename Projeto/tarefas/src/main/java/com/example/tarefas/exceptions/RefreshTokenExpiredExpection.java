package com.example.tarefas.exceptions;

public class RefreshTokenExpiredExpection extends RuntimeException {
    public RefreshTokenExpiredExpection(String message) {
        super(message);
    }
}
