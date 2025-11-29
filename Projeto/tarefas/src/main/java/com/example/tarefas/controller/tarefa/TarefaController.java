package com.example.tarefas.controller.tarefa;

import com.example.tarefas.controller.tarefa.dto.TarefaDto;
import com.example.tarefas.model.Tarefa;
import com.example.tarefas.service.tarefa.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas")
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }


    @Operation(summary = "Listar todas as tarefas")
    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllTarefas() {
        return ResponseEntity.ok(tarefaService.findAll());
    }


    @Operation(
            summary = "Buscar tarefa pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTarefaById(
            @Parameter(description = "ID da tarefa", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.findById(id));
    }


    @Operation(
            summary = "Criar nova tarefa",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da tarefa",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TarefaDto.class),
                            examples = @ExampleObject(value = """
                            {
                              "titulo": "Pagar boletos",
                              "descricao": "Pagar até 10/12",
                              "usuarioId": 1,
                              "urgencia": "ALTA"
                            }
                            """)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@Valid @RequestBody TarefaDto tarefaDto) {
        return ResponseEntity.ok(tarefaService.create(tarefaDto));
    }


    @Operation(summary = "Atualizar tarefa existente")
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Valid @RequestBody TarefaDto tarefaDto,
            @Parameter(description = "ID do usuário dono da tarefa") @RequestParam Long usuarioId
    ) {
        return ResponseEntity.ok(tarefaService.update(id, tarefaDto, usuarioId));
    }


    @Operation(summary = "Marcar tarefa como concluída")
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> concluirTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "ID do usuário dono da tarefa") @RequestParam Long usuarioId
    ) {
        return ResponseEntity.ok(tarefaService.concluirTarefa(id, usuarioId));
    }


    @Operation(summary = "Marcar tarefa como pendente")
    @PatchMapping("/{id}/pendente")
    public ResponseEntity<Tarefa> pendenteTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @Parameter(description = "ID do usuário dono da tarefa") @RequestParam Long usuarioId
    ) {
        return ResponseEntity.ok(tarefaService.pendenteTarefa(id, usuarioId));
    }


    @Operation(
            summary = "Excluir tarefa definitivamente",
            parameters = {
                    @Parameter(name = "usuarioId", description = "ID do usuário dono")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarefa(
            @Parameter(description = "ID da tarefa") @PathVariable Long id,
            @RequestParam Long usuarioId) {

        tarefaService.delete(id, usuarioId);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }


    @Operation(
            summary = "Importar tarefas via arquivo CSV",
            description = "Formato esperado: titulo,descricao,usuarioId,urgencia"
    )
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importTarefas(
            @Parameter(description = "Arquivo CSV") @RequestParam("file") MultipartFile file
    ) {
        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("Envie um arquivo CSV válido!");
            }
            int imported = tarefaService.importFromCsv(file);
            return ResponseEntity.ok(imported + " tarefas importadas com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao importar arquivo: " + e.getMessage());
        }
    }


    @Operation(summary = "Buscar tarefas por nível de urgência")
    @GetMapping("/urgencia/{urgencia}")
    public ResponseEntity<List<Tarefa>> getByUrgencia(
            @PathVariable String urgencia
    ) {
        return ResponseEntity.ok(tarefaService.findByUrgencia(urgencia));
    }

}




