package com.example.tarefas.repository;

import com.example.tarefas.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByTarefaId(Long idTarefa);
}
