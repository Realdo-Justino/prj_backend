package com.example.tarefas.service.tarefa;

import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.enums.Urgencia;
import com.example.tarefas.exceptions.BadRequestException;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.TarefaRepository;
import com.example.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    @Transactional
    public Tarefa findById(long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
    }


    public Tarefa create(TarefaDto tarefaDto) {
        Usuario usuario = usuarioRepository.findById(tarefaDto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario_criado(usuario)
                .urgencia(tarefaDto.urgencia())
                .concluido(false)
                .build();

        return tarefaRepository.save(tarefa);
    }


    public Tarefa update(Long id, TarefaDto tarefaDto, Long usuarioId) {
        Tarefa existente = findById(id);

        if (!existente.getUsuario_criado().getId().equals(usuarioId)) {
            throw new BadRequestException("Você não tem permissão para alterar esta tarefa.");
        }

        existente.setTitulo(tarefaDto.titulo());
        existente.setDescricao(tarefaDto.descricao());
        existente.setUrgencia(tarefaDto.urgencia());

        return tarefaRepository.save(existente);
    }


    public Tarefa concluirTarefa(Long id, Long usuarioId) {
        Tarefa tarefa = findById(id);

        if (!tarefa.getUsuario_criado().getId().equals(usuarioId)) {
            throw new BadRequestException("Você não tem permissão para concluir esta tarefa.");
        }

        tarefa.setConcluido(true);
        return tarefaRepository.save(tarefa);
    }


    public Tarefa pendenteTarefa(Long id, Long usuarioId) {
        Tarefa tarefa = findById(id);

        if (!tarefa.getUsuario_criado().getId().equals(usuarioId)) {
            throw new BadRequestException("Você não tem permissão para alterar esta tarefa.");
        }

        tarefa.setConcluido(false);
        return tarefaRepository.save(tarefa);
    }

    public void delete(Long id, Long usuarioId) {
        Tarefa existente = findById(id);

        if (!existente.getUsuario_criado().getId().equals(usuarioId)) {
            throw new BadRequestException("Você não tem permissão para deletar esta tarefa.");
        }

        tarefaRepository.deleteById(id);
    }


    public List<Tarefa> findByUrgencia(String urgencia) {
        Urgencia nivel = Urgencia.valueOf(urgencia.toUpperCase());
        return tarefaRepository.findByUrgencia(nivel);
    }


    public int importFromCsv(MultipartFile file) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] dados = line.split(",");

                if (dados.length >= 3) {
                    Long usuarioId = Long.parseLong(dados[2].trim());

                    Urgencia urgencia = Urgencia.MEDIA;
                    if (dados.length >= 4) {
                        try {
                            urgencia = Urgencia.valueOf(dados[3].trim().toUpperCase());
                        } catch (Exception ignored) {}
                    }

                    Tarefa tarefa = Tarefa.builder()
                            .titulo(dados[0].trim())
                            .descricao(dados[1].trim())
                            .usuario_criado(usuarioRepository.findById(usuarioId)
                                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado")))
                            .urgencia(urgencia)
                            .concluido(false)
                            .build();

                    tarefaRepository.save(tarefa);
                    count++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar CSV: " + e.getMessage());
        }

        return count;
    }
}
