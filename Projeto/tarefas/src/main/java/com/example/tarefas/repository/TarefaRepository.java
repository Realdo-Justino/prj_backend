package com.example.tarefas.repository;

import com.example.tarefas.enums.Urgencia;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByUrgencia(Urgencia urgencia);
    @Query("SELECT t FROM Tarefa t WHERE t.usuario_criado = :usuario")
    List<Tarefa> findByUsuarioCriado(@Param("usuario") Usuario usuario);

    @Query("SELECT t FROM Tarefa t WHERE t.usuario_criado = :usuario AND t.urgencia = :urgencia")
    List<Tarefa> findByUsuarioCriadoAndUrgencia(@Param("usuario") Usuario usuario, @Param("urgencia") Urgencia urgencia);


}
