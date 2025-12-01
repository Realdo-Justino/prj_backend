package com.example.tarefas.controller.tarefa.dto;

import com.example.tarefas.enums.Urgencia;


import com.example.tarefas.enums.Urgencia;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record TarefaDto(

        @Schema(description = "Título da tarefa a ser realizada",
                example = "Finalizar relatório de desempenho")
        @NotNull(message = "O título é obrigatório")
        String titulo,

        @Schema(description = "Descrição detalhada da tarefa",
                example = "Preparar o relatório com métricas de produtividade")
        String descricao,

        @Schema(description = "Identificador do usuário responsável pela tarefa",
                example = "3")
        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId,

        @Schema(description = "Nivel de urgência da tarefa",
                example = "ALTA",
                allowableValues = {"BAIXA", "MEDIA", "ALTA"})
        @NotNull(message = "A urgência é obrigatória")
        Urgencia urgencia
) {}

