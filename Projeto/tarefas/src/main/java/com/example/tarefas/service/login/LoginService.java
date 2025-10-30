package com.example.tarefas.service.login;

import com.example.tarefas.controller.login.dto.LoginDto;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UsuarioService usuarioService;

    public void validate(LoginDto loginDto) {
        Usuario usuario = usuarioService.findByEmail(loginDto.email());

        if(usuario.getSenha() != loginDto.senha()) {
            throw new EntityNotFoundException("Usuario/Senha invalido");
        }
    }
}
