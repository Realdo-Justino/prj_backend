package com.example.tarefas.model;

import com.example.tarefas.enums.Urgencia;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioCriado;

    private String titulo = "";
    private String descricao = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Urgencia urgencia;
}
