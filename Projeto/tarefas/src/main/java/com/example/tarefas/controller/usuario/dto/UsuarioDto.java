package com.example.tarefas.controller.usuario.dto;

public record UsuarioDto (
    String nome,
    String sobrenome,
    String senha,
    String email
) {
}
