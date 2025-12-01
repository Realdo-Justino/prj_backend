package com.example.tarefas.controller.historico.dto;

import com.example.tarefas.model.HistoricoTarefa;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoricoResponseDto {

    private Long id;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFinalizacao;
    private Long idTarefa;
    private Long idUsuario;

    public HistoricoResponseDto(HistoricoTarefa historico) {
        this.id = historico.getId();
        this.dataCriacao = historico.getDataCriacao();
        this.dataFinalizacao = historico.getDataFinalizacao();
        this.idTarefa = historico.getTarefa().getId();
        this.idUsuario = historico.getUsuario().getId();
    }
}