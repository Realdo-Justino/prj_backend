package com.example.tarefas.repository;

import com.example.tarefas.model.HistoricoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<HistoricoTarefa, Long> {
    List<HistoricoTarefa> findByTarefaId(Long idTarefa);
    List<HistoricoTarefa> findByUsuarioId(Long idUsuario);
}
