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
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Usuario> getAllUsuarios(@PathVariable Long userId) {
        return ResponseEntity.ok(usuarioService.findById(userId));
    }

    @Operation(summary = "Create a user", description = "Creates a new application user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.create(usuarioDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long userId, @Valid @RequestBody UsuarioDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.update(userId, usuarioDto));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Usuario> activateUsuario(@PathVariable Long userId, @Valid @RequestBody PatchUsuarioDto patchUsuarioDto) {
        if(patchUsuarioDto.ativo()) return ResponseEntity.ok(usuarioService.activate(userId));

        return ResponseEntity.ok(usuarioService.deActivate(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long userId) {
        usuarioService.delete(userId);

        return ResponseEntity.ok().body("Deletado com sucesso");
    }
}
