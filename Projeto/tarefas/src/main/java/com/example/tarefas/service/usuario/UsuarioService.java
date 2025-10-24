package com.example.tarefas.service.usuario;

import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() { return usuarioRepository.findAll(); }
}
