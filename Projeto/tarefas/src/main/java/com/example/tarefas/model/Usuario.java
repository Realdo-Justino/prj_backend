package com.example.tarefas.model;

import jakarta.persistence.*;

import java.time.LocalDate;

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String senha;
    private String email;
    private LocalDate data_criacao;
    private Boolean ativo;
}
