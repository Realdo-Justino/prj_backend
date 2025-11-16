package com.example.tarefas.controller.historico;

import com.example.tarefas.controller.historico.dto.HistoricoResponseDto;
import com.example.tarefas.service.historico.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Lista todo o histórico de uma tarefa")
    @GetMapping("/tarefa/{id}")
    public ResponseEntity<List<HistoricoResponseDto>> listar(@PathVariable Long id) {

        List<HistoricoResponseDto> resposta = historicoService.listarPorTarefa(id)
                .stream()
                .map(HistoricoResponseDto::new)
                .toList();

        return ResponseEntity.ok(resposta);
    }
}