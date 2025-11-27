package com.example.tarefas.controller.usuario;

import com.example.tarefas.controller.usuario.dto.PatchUsuarioDto;
import com.example.tarefas.controller.usuario.dto.UsuarioDto;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Pesquisar usuarios", description = "Pesquisa todos os usarios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios"),
    })
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Pesquisar usuario", description = "Pesquisa o usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario nao encontrado\" }"
                )
            )
        )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Usuario> getAllUsuarios(@PathVariable Long userId) {
        return ResponseEntity.ok(usuarioService.findById(userId));
    }

    @Operation(summary = "Criar usuario", description = "Cria um novo usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario Criado"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Email ja em uso\" }"
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.create(usuarioDto));
    }

    @Operation(summary = "Atualizar usuario", description = "Atualiza o usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario nao encontrado\" }"
                )
            )
        )
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long userId, @Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.update(userId, usuarioDto));
    }

    @Operation(summary = "Ativar/Desativar usuario", description = "Ativa/Desativa o usuario por id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso"),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario nao encontrado\" }"
                )
            )
        )
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<Usuario> activateUsuario(@PathVariable Long userId, @Valid @RequestBody PatchUsuarioDto patchUsuarioDto) {
        if(patchUsuarioDto.ativo()) return ResponseEntity.ok(usuarioService.activate(userId));

        return ResponseEntity.ok(usuarioService.deActivate(userId));
    }

    @Operation(summary = "Deletar usuario", description = "Deleta o usuario por id")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso",
            content = @Content(
                mediaType = "text/plain",
                schema = @Schema(type = "string", example = "Deletado com sucesso")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario nao encontrado\" }"
                )
            )
        )
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long userId) {
        usuarioService.delete(userId);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }

    @Operation(summary = "Importar usuarios", description = "Importa usuarios por um arquivo CSV")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sucesso",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "X usuarios importados com sucesso")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Falha ao importar o CSV: XXX")
                    )
            )
    })
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<String> importTasks(
        @Parameter(description = "CSV file to import", required = true)
        @RequestParam("file") MultipartFile file) {

        try {
            if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest().body("Arquivo invalido. Envie um CSV");
            }

            int imported = usuarioService.importFromCsv(file);
            return ResponseEntity.ok(imported + " usuarios importados com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Falha ao importar o CSV: " + e.getMessage());
        }
    }
}
