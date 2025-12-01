package com.example.tarefas.repository;

import com.example.tarefas.enums.Urgencia;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
