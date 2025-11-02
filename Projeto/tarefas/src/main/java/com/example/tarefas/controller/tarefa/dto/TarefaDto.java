package com.example.tarefas.controller.tarefa.dto;

public record TarefaDto(
        String titulo,
        String descricao,
        Long usuarioId
) {
}
