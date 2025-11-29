package com.example.tarefas.repository;

import com.example.tarefas.enums.Urgencia;
import com.example.tarefas.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUrgencia(Urgencia urgencia);
}
