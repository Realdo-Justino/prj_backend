package com.example.tarefas.controller.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
    @Email(message = "Email deve ser valido") @NotBlank(message = "Email e obrigatorio") @NotNull(message = "Email e obrigatorio") String email,
    @NotBlank(message = "Senha e obrigatorio") @NotNull(message = "Senha e obrigatorio") String senha
) {
}
