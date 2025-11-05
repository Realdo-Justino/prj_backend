package com.example.tarefas.controller.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.service.login.LoginService;
import com.example.tarefas.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) { this.loginService = loginService; }

    @Operation(summary = "Realizar Login", description = "Realiza o login do usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro")
    })
    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
       loginService.validate(loginDto);

       return ResponseEntity.ok().body("Usuario Valido");
    }
}
