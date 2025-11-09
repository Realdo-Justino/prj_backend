package com.example.tarefas.controller.tarefa;

import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.service.tarefa.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllTarefas() {
        return ResponseEntity.ok(tarefaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTarefaById(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@Valid @RequestBody TarefaDto tarefaDto) {
        return ResponseEntity.ok(tarefaService.create(tarefaDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(
            @PathVariable Long id,
            @Valid @RequestBody TarefaDto tarefaDto,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.update(id, tarefaDto, usuarioId));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> concluirTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.concluirTarefa(id, usuarioId));
    }

    @PatchMapping("/{id}/pendente")
    public ResponseEntity<Tarefa> pendenteTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.pendenteTarefa(id, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        tarefaService.delete(id, usuarioId);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
}
