package com.example.tarefas.controller.usuario.dto;

import jakarta.validation.constraints.NotNull;

public record PatchUsuarioDto(
    @NotNull(message = "ativo e obrigatorio") boolean ativo
) {
}
