package com.example.tarefas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_tarefa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoTarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataFinalizacao;
}