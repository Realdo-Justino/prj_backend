package com.example.tarefas.service.historico;

import com.example.tarefas.model.HistoricoTarefa;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.repository.HistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    public void registrarCriacao(Tarefa tarefa) {
        HistoricoTarefa historico = HistoricoTarefa.builder()
                .tarefa(tarefa)
                .acao("CRIACAO")
                .dataHora(LocalDateTime.now())
                .build();

        historicoRepository.save(historico);
    }

    public void registrarAcao(Tarefa tarefa, String acao, String descricao) {
        HistoricoTarefa historico = HistoricoTarefa.builder()
                .tarefa(tarefa)
                .usuario(tarefa.getUsuario())
                .acao(acao)
                .descricao(descricao)
                .dataHora(LocalDateTime.now())
                .build();

        historicoRepository.save(historico);
    }

    public List<HistoricoTarefa> listarPorTarefa(Long idTarefa) {
        return historicoRepository.findByTarefaId(idTarefa);
    }
}