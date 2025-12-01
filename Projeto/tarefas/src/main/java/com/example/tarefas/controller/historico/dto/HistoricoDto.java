package com.example.tarefas.controller.historico.dto;

import jakarta.validation.constraints.NotNull;

public record HistoricoDto (
    @NotNull(message = "Tarefa e obrigatoria") Integer idTarefa,
) {
}
