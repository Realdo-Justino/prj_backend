package com.example.tarefas.controller.historico.dto;

import jakarta.validation.constraints.NotNull;

public record HistoricoDto (
    @NotNull(message = "Tarefa e obrigatoria") Integer idTarefa,
    @NotNull(message = "Usuario e obrigatoria") Integer idUsuario
) {
}
