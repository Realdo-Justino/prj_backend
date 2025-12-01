package com.example.tarefas.controller.historico;

import com.example.tarefas.controller.historico.dto.HistoricoDto;
import com.example.tarefas.controller.historico.dto.HistoricoResponseDto;
import com.example.tarefas.service.historico.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Histórico")
@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    @Operation(summary = "Listar um registro de histórico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada"),
            @ApiResponse(
                responseCode = "400",
                description = "Erro",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{ \"error\": \"Historico nao encontrado\" }"
                    )
                )
            )
    })
    @GetMapping("/{idHistorico}")
    public ResponseEntity<HistoricoResponseDto> listarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new HistoricoResponseDto(historicoService.findById(id)));
    }

    @Operation(summary = "Listar histórico de um usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada")
    })
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<HistoricoResponseDto>> listarPorUsuario(@PathVariable Long idUsuario) {
        var lista = historicoService.listarPorUsuario(idUsuario)
                .stream()
                .map(HistoricoResponseDto::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Listar histórico de uma tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada")
    })
    @GetMapping("/tarefa/{idTarefa}")
    public ResponseEntity<List<HistoricoResponseDto>> listar(@PathVariable Long idTarefa) {
        var lista = historicoService.listarPorTarefa(idTarefa)
                .stream()
                .map(HistoricoResponseDto::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Iniciar uma tarefa", description = "Inicia uma tarefa, criando um historico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa iniciada"),
        @ApiResponse(
                responseCode = "400",
                description = "Erro",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                value = "{ \"error\": \"Tarefa nao encontrada\" }"
                        )
                )
        )
    })
    @PostMapping
    public ResponseEntity<HistoricoResponseDto> criarHistorico(@Valid @RequestBody HistoricoDto historicoDto) {
        return ResponseEntity.ok(new HistoricoResponseDto(historicoService.registrarCriacao(historicoDto)));
    }

    @Operation(summary = "Finaliza uma tarefa", description = "Finaliza uma tarefa, alterando o historico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tarefa Finalizada"),
        @ApiResponse(
                responseCode = "400",
                description = "Erro",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(
                                value = "{ \"error\": \"Historico nao encontrada\" }"
                        )
                )
        )
    })
    @PatchMapping("/{idHistorico}")
    public ResponseEntity<HistoricoResponseDto> finalizarHistorico(@PathVariable long idHistorico) {
        return ResponseEntity.ok(new HistoricoResponseDto(historicoService.registrarFinalizacao(idHistorico)));
    }
}