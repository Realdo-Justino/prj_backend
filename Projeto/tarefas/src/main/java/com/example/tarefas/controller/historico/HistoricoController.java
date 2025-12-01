package com.example.tarefas.controller.historico;

import com.example.tarefas.controller.historico.dto.HistoricoDto;
import com.example.tarefas.controller.historico.dto.HistoricoResponseDto;
import com.example.tarefas.service.historico.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Histórico")
@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    @Operation(summary = "Listar histórico de uma tarefa")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistoricoResponseDto.class)
                    )
            )
    })
    @GetMapping("/{idTarefa}")
    public ResponseEntity<List<HistoricoResponseDto>> listar(@PathVariable Long idTarefa) {
        var lista = historicoService.listarPorTarefa(idTarefa)
                .stream()
                .map(HistoricoResponseDto::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<HistoricoResponseDto> createHistory(@Valid @RequestBody HistoricoDto historicoDto) {
        return ResponseEntity.ok(new HistoricoResponseDto(historicoService.registrarCriacao(historicoDto)));
    }
}