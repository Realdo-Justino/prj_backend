package com.example.tarefas.service.historico;

import com.example.tarefas.model.Historico;
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
        Historico historico = Historico.builder()
                .tarefa(tarefa)
                .dataCriacao(LocalDateTime.now())
                .build();

        historicoRepository.save(historico);
    }

    public void registrarFinalizacao(Tarefa tarefa) {
        List<Historico> lista = historicoRepository.findByTarefaId(tarefa.getId());

        if (lista.isEmpty()) return;

        Historico ultimo = lista.get(lista.size() - 1);
        ultimo.setDataFinalizacao(LocalDateTime.now());

        historicoRepository.save(ultimo);
    }

    public List<Historico> listarPorTarefa(Long idTarefa) {
        return historicoRepository.findByTarefaId(idTarefa);
    }
}