package com.example.tarefas.service.tarefa;

import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.enums.Urgencia;
import com.example.tarefas.exceptions.BadRequestException;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.TarefaRepository;
import com.example.tarefas.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    @Transactional
    public Tarefa findById(long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        if(tarefa.isPresent()) {
            return tarefa.get();
        }

        throw  new EntityNotFoundException("Tarefa nao encontrada");
    }

    public Tarefa create(TarefaDto tarefaDto) {
        Usuario usuario = usuarioService.getUsuarioLogado();

        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuarioCriado(usuario)
                .urgencia(tarefaDto.urgencia())
                .build();

        return tarefaRepository.save(tarefa);
    }

    public Tarefa update(Long id, TarefaDto tarefaDto) {
        Usuario usuario = usuarioService.getUsuarioLogado();

        Tarefa existente = findById(id);
        existente.setTitulo(tarefaDto.titulo());
        existente.setDescricao(tarefaDto.descricao());
        existente.setUrgencia(tarefaDto.urgencia());
        existente.setUsuarioCriado(usuario);

        return tarefaRepository.save(existente);
    }

    public void delete(Long id) {
        Tarefa tarefa = findById(id);

        tarefaRepository.delete(tarefa);
    }

    public int importFromCsv(MultipartFile file) {
        int count = 0;
        Usuario usuarioLogado = usuarioService.getUsuarioLogado();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] dados = line.split(",");
                if (dados.length >= 2) {
                    Urgencia urgencia = Urgencia.MEDIA;
                    if (dados.length >= 3) {
                        try {
                            urgencia = Urgencia.valueOf(dados[2].trim().toUpperCase());
                        } catch (Exception ignored) {}
                    }
                    Tarefa tarefa = Tarefa.builder()
                            .titulo(dados[0].trim())
                            .descricao(dados[1].trim())
                            .urgencia(urgencia)
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
