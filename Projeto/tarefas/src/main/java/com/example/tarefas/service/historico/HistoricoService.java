package com.example.tarefas.service.historico;

import com.example.tarefas.model.HistoricoTarefa;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.HistoricoRepository;
import com.example.tarefas.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    @Autowired
    private final UsuarioService usuarioService;

    public void registrarCriacao(Tarefa tarefa) {
        //Usuario usuario = usuarioService.findByEmail();

        HistoricoTarefa historico = HistoricoTarefa.builder()
                .tarefa(tarefa)
                .dataCriacao(LocalDateTime.now())
                .build();

        historicoRepository.save(historico);
    }

    public void registrarAcao(Tarefa tarefa, String acao, String descricao) {
        HistoricoTarefa historico = HistoricoTarefa.builder()
                .tarefa(tarefa)
                .usuario(tarefa.getUsuarioCriado())
                .dataFinalizacao(LocalDateTime.now())
                .build();

        historicoRepository.save(historico);
    }

    public List<HistoricoTarefa> listarPorTarefa(Long idTarefa) {
        return historicoRepository.findByTarefaId(idTarefa);
    }
}