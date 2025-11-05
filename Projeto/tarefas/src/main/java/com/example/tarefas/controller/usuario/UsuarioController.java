package com.example.tarefas.controller.usuario;

import com.example.tarefas.controller.usuario.dto.PatchUsuarioDto;
import com.example.tarefas.controller.usuario.dto.UsuarioDto;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Pesquisar usuario", description = "Pesquisa o usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Usuario> getAllUsuarios(@PathVariable Long userId) {
        return ResponseEntity.ok(usuarioService.findById(userId));
    }

    @Operation(summary = "Criar usuario", description = "Cria um novo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Criado"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.create(usuarioDto));
    }

    @Operation(summary = "Atualizar usuario", description = "Atualiza o usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long userId, @Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.update(userId, usuarioDto));
    }

    @Operation(summary = "Ativar/Desativar usuario", description = "Ativa/Desativa o usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @PatchMapping("/{userId}")
    public ResponseEntity<Usuario> activateUsuario(@PathVariable Long userId, @Valid @RequestBody PatchUsuarioDto patchUsuarioDto) {
        if(patchUsuarioDto.ativo()) return ResponseEntity.ok(usuarioService.activate(userId));

        return ResponseEntity.ok(usuarioService.deActivate(userId));
    }

    @Operation(summary = "Deletar usuario", description = "Deleta o usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long userId) {
        usuarioService.delete(userId);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }
}
