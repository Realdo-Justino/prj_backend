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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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

    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista com todas as tarefas cadastradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllTarefas() {
        return ResponseEntity.ok(tarefaService.findAll());
    }

    @Operation(summary = "Buscar tarefas por ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro - Tarefa não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Tarefa não encontrada\" }")
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTarefaById(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.findById(id));
    }

    @Operation(summary = "Criar tarefas", description = "Cria uma nova tarefa no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na criação da tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Usuário não encontrado ou dados inválidos\" }")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Tarefa> createTarefa(@Valid @RequestBody TarefaDto tarefaDto) {
        return ResponseEntity.ok(tarefaService.create(tarefaDto));
    }

    @Operation(summary = "Atualizar tarefas", description = "Atualiza os dados de uma tarefa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao atualizar tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Você não tem permissão para atualizar esta tarefa\" }")
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTarefa(
            @PathVariable Long id,
            @Valid @RequestBody TarefaDto tarefaDto,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.update(id, tarefaDto, usuarioId));
    }

    @Operation(summary = "Concluir tarefas", description = "Marca uma tarefa como concluída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa marcada como concluída com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao concluir tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Você não tem permissão para alterar esta tarefa\" }")
                    )
            )
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Tarefa> concluirTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.concluirTarefa(id, usuarioId));
    }

    @Operation(summary = "Marcar tarefas como pendente", description = "Define o status da tarefa como pendente (não concluída)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa marcada como pendente com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao alterar status da tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Você não tem permissão para alterar esta tarefa\" }")
                    )
            )
    })
    @PatchMapping("/{id}/pendente")
    public ResponseEntity<Tarefa> pendenteTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        return ResponseEntity.ok(tarefaService.pendenteTarefa(id, usuarioId));
    }

    @Operation(summary = "Deletar tarefas", description = "Remove uma tarefa do sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarefa deletada com sucesso",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Tarefa deletada com sucesso")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro ao deletar tarefa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"error\": \"Você não tem permissão para deletar esta tarefa\" }")
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTarefa(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        tarefaService.delete(id, usuarioId);
        return ResponseEntity.ok("Tarefa deletada com sucesso");
    }
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importTarefas(
            @Parameter(description = "Arquivo CSV para importar tarefas", required = true)
            @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("Arquivo inválido. Envie um CSV");
            }

            int imported = tarefaService.importFromCsv(file);
            return ResponseEntity.ok(imported + " tarefas importadas com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Falha ao importar o CSV: " + e.getMessage());
        }
    }

}
