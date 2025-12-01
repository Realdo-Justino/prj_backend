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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private Usuario getUsuarioLogado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));
    }

    @Transactional
    public List<Tarefa> findAll() {
        Usuario usuario = getUsuarioLogado();
        return tarefaRepository.findByUsuarioCriado(usuario);
    }

    @Transactional
    public Tarefa findById(long id) {
        Usuario usuario = getUsuarioLogado();
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada"));
        if (!tarefa.getUsuario_criado().getId().equals(usuario.getId())) {
            throw new BadRequestException("Você não tem permissão para acessar esta tarefa.");
        }
        return tarefa;
    }

    public Tarefa create(TarefaDto tarefaDto) {
        Usuario usuario = getUsuarioLogado();
        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario_criado(usuario)
                .urgencia(tarefaDto.urgencia())
                .concluido(false)
                .build();
        return tarefaRepository.save(tarefa);
    }

    public Tarefa update(Long id, TarefaDto tarefaDto) {
        Tarefa existente = findById(id);
        existente.setTitulo(tarefaDto.titulo());
        existente.setDescricao(tarefaDto.descricao());
        existente.setUrgencia(tarefaDto.urgencia());
        return tarefaRepository.save(existente);
    }

    public Tarefa concluirTarefa(Long id) {
        Tarefa tarefa = findById(id);
        tarefa.setConcluido(true);
        return tarefaRepository.save(tarefa);
    }

    public Tarefa pendenteTarefa(Long id) {
        Tarefa tarefa = findById(id);
        tarefa.setConcluido(false);
        return tarefaRepository.save(tarefa);
    }

    public void delete(Long id) {
        Tarefa tarefa = findById(id);
        tarefaRepository.delete(tarefa);
    }

    public List<Tarefa> findByUrgencia(String urgencia) {
        Usuario usuario = getUsuarioLogado();
        Urgencia nivel = Urgencia.valueOf(urgencia.toUpperCase());
        return tarefaRepository.findByUsuarioCriadoAndUrgencia(usuario, nivel);
    }

    public int importFromCsv(MultipartFile file) {
        int count = 0;
        Usuario usuarioLogado = getUsuarioLogado();
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
                            .usuario_criado(usuarioLogado)
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
