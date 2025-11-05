package com.example.tarefas.controller.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.service.login.LoginService;
import com.example.tarefas.service.usuario.UsuarioService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autorização")
@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) { this.loginService = loginService; }

    @Operation(summary = "Realizar Login", description = "Realiza o login do usuario")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sucesso",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Usuario Valido")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Erro",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{ \"error\": \"Usuario/Senha invalido\" }"
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
       loginService.validate(loginDto);

       return ResponseEntity.ok().body("Usuario Valido");
    }
}
