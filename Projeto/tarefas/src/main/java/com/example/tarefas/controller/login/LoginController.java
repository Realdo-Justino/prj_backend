package com.example.tarefas.controller.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.service.login.LoginService;
import com.example.tarefas.service.usuario.UsuarioService;
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

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto) {
       loginService.validate(loginDto);

       return ResponseEntity.ok().body("Usuario Valido");
    }
}
