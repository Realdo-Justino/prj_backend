package com.example.tarefas.service.tarefa;


import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.controller.usuario.dto.UsuarioDto;
import com.example.tarefas.exceptions.BadRequestException;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.TarefaRepository;
import com.example.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<Tarefa> findAll() { return tarefaRepository.findAll(); }

    @Transactional
    public Tarefa findById(long id) {
        Optional<Tarefa> task  = tarefaRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        }

        throw new EntityNotFoundException("Tarefa nao encontrado");
    }






    public Tarefa create(TarefaDto tarefaDto) {
        Usuario usuario = usuarioRepository.findById(tarefaDto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario_criado(usuario)
                .concluido(false)
                .build();

        return tarefaRepository.save(tarefa);
    }

    public Tarefa update(Long id, TarefaDto tarefaDto, Long usuarioId) {
        Tarefa tarefaExistente = findById(id);

        if (!tarefaExistente.getUsuario_criado().getId().equals(tarefaDto.usuarioId())) {
            throw new BadRequestException("Você não tem permissão para alterar esta tarefa.");
        }


        Usuario usuario = usuarioRepository.findById(tarefaDto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tarefa tarefaAtualizada = Tarefa.builder()
                .id(tarefaExistente.getId())
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario_criado(usuario)
                .build();

        return tarefaRepository.save(tarefaAtualizada);
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
        Tarefa tarefaExistente = findById(id);
        if (!tarefaExistente.getUsuario_criado().getId().equals(usuarioId)) {
            throw new BadRequestException("Você não tem permissão para deletar esta tarefa.");
        }
        tarefaRepository.deleteById(id);
    }

}
