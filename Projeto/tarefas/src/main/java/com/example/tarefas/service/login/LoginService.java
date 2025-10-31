package com.example.tarefas.service.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.service.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class LoginService {

    @Autowired
    private UsuarioService usuarioService;

    public void validate(LoginDto loginDto) {
        Usuario usuario = usuarioService.findByEmail(loginDto.email());

        if(!usuario.getSenha().equals(loginDto.senha())) {
            throw new BadCredentialsException("Usuario/Senha invalido");
        }
    }
}
