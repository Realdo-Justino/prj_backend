package com.example.tarefas.service.tarefa;


import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.controller.usuario.dto.UsuarioDto;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.TarefaRepository;
import com.example.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {
    @Autowired
    TarefaRepository tarefaRepository;

    public List<Tarefa> findAll() { return tarefaRepository.findAll(); }

    public Tarefa findById(long id) {
        Optional<Tarefa> task  = tarefaRepository.findById(id);
        if(task.isPresent()) {
            return task.get();
        }

        throw new EntityNotFoundException("Tarefa nao encontrado");
    }




    @Autowired
    private UsuarioRepository usuarioRepository;

    public Tarefa create(TarefaDto tarefaDto) {
        Usuario usuario = usuarioRepository.findById(tarefaDto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Tarefa tarefa = Tarefa.builder()
                .titulo(tarefaDto.titulo())
                .descricao(tarefaDto.descricao())
                .usuario_criado(usuario)
                .build();

        return tarefaRepository.save(tarefa);
    }

    public Tarefa update(Long id, TarefaDto tarefaDto) {
        Tarefa tarefaExistente = findById(id);

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

    public void delete(Long id) {
        findById(id); // garante que a tarefa existe, senão lança exceção
        tarefaRepository.deleteById(id);
    }

}
