package com.example.tarefas.service.historico;

import com.example.tarefas.controller.historico.dto.HistoricoDto;
import com.example.tarefas.exceptions.BadRequestException;
import com.example.tarefas.model.HistoricoTarefa;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.HistoricoRepository;
import com.example.tarefas.service.tarefa.TarefaService;
import com.example.tarefas.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private final HistoricoRepository historicoRepository;

    @Autowired
    private final UsuarioService usuarioService;

    @Autowired
    private final TarefaService tarefaService;

    @Transactional
    public HistoricoTarefa findById(long id) {
        Optional<HistoricoTarefa> historicoTarefa = historicoRepository.findById(id);
        if(historicoTarefa.isPresent()) {
            return historicoTarefa.get();
        }

        throw  new EntityNotFoundException("Historico nao encontrado");
    }

    public List<HistoricoTarefa> listarPorTarefa(Long idTarefa) {
        return historicoRepository.findByTarefaId(idTarefa);
    }

    public List<HistoricoTarefa> listarPorUsuario(Long idUsuario) {
        return historicoRepository.findByUsuarioId(idUsuario);
    }

    public HistoricoTarefa registrarCriacao(HistoricoDto historicoDto) {
        Usuario usuario = usuarioService.findById(historicoDto.idUsuario());
        Tarefa tarefa = tarefaService.findById(historicoDto.idTarefa());

        HistoricoTarefa historico = HistoricoTarefa.builder()
                .tarefa(tarefa)
                .usuario(usuario)
                .dataCriacao(LocalDateTime.now())
                .build();

        return historicoRepository.save(historico);
    }

    public HistoricoTarefa registrarFinalizacao(Long idHistorico, Long idUsuario) {
        HistoricoTarefa historico = findById(idHistorico);

        if (!historico.getUsuario().getId().equals(idUsuario)) {
            throw new BadRequestException("Você não tem permissão para alterar esta tarefa.");
        }

        HistoricoTarefa historicoAtualizado = HistoricoTarefa.builder()
                .tarefa(historico.getTarefa())
                .usuario(historico.getUsuario())
                .dataCriacao(historico.getDataCriacao())
                .dataFinalizacao(LocalDateTime.now())
                .build();

        return historicoRepository.save(historico);
    }
}