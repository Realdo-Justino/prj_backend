package com.example.tarefas.controller.tarefa;

import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.service.tarefa.TarefaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefa")
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
    public ResponseEntity<Tarefa> createTarefa(@RequestBody TarefaDto tarefaDto) {
        return ResponseEntity.ok(tarefaService.create(tarefaDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(@PathVariable Long id, @RequestBody TarefaDto tarefaDto) {
        return ResponseEntity.ok(tarefaService.update(id, tarefaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarefa(@PathVariable Long id) {
        tarefaService.delete(id);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
}
