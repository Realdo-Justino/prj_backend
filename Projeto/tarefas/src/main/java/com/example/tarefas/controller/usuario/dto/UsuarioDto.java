package com.example.tarefas.controller.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto (
    @NotBlank(message = "Nome e obrigatorio") @NotNull(message = "Nome e obrigatorio") String nome,
    @NotBlank(message = "Sobrenome e obrigatorio") @NotNull(message = "Sobrenome e obrigatorio") String sobrenome,
    @NotBlank(message = "Senha e obrigatorio") @NotNull(message = "Senha e obrigatorio") String senha,
    @NotBlank(message = "Email e obrigatorio") @NotNull(message = "Email e obrigatorio") String email
) {
}
