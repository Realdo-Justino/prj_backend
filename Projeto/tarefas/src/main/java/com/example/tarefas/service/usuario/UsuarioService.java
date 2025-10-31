package com.example.tarefas.service.usuario;

import com.example.tarefas.controller.usuario.dto.UsuarioDto;
import com.example.tarefas.exceptions.BadRequestException;
import com.example.tarefas.model.Usuario;
import com.example.tarefas.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public List<Usuario> findAll() { return usuarioRepository.findAll(); }

    @Transactional
    public Usuario findById(long id) {
        Optional<Usuario> user = usuarioRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException("Usuario nao encontrado");
    }

    @Transactional
    public Usuario findByEmail(String email) {
        Optional<Usuario> user = usuarioRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        }

        throw new EntityNotFoundException("Usuario nao encontrado");
    }

    public Usuario create(UsuarioDto usuarioDto) {
        try {
            findByEmail(usuarioDto.email());

            throw new BadRequestException("Email ja em uso");
        } catch(EntityNotFoundException entityNotFoundException) {
        }

        Usuario usuario = Usuario.builder()
                .nome(usuarioDto.nome())
                .sobrenome(usuarioDto.sobrenome())
                .senha(usuarioDto.senha())
                .email(usuarioDto.email())
                .dataCriacao(LocalDate.now())
                .ativo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, UsuarioDto usuarioDto) {
        Usuario searchedUsuario = findById(id);

        Usuario usuario = Usuario.builder()
                .id(searchedUsuario.getId())
                .nome(usuarioDto.nome())
                .sobrenome(usuarioDto.sobrenome())
                .senha(usuarioDto.senha())
                .email(usuarioDto.email())
                .dataCriacao(searchedUsuario.getDataCriacao())
                .ativo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario activate(Long id) {
        Usuario searchedUsuario = findById(id);

        Usuario usuario = Usuario.builder()
                .id(searchedUsuario.getId())
                .nome(searchedUsuario.getNome())
                .sobrenome(searchedUsuario.getSobrenome())
                .senha(searchedUsuario.getSenha())
                .email(searchedUsuario.getEmail())
                .dataCriacao(searchedUsuario.getDataCriacao())
                .ativo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario deActivate(Long id) {
        Usuario searchedUsuario = findById(id);

        Usuario usuario = Usuario.builder()
                .id(searchedUsuario.getId())
                .nome(searchedUsuario.getNome())
                .sobrenome(searchedUsuario.getSobrenome())
                .senha(searchedUsuario.getSenha())
                .email(searchedUsuario.getEmail())
                .dataCriacao(searchedUsuario.getDataCriacao())
                .ativo(false)
                .build();

        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        findById(id);

        usuarioRepository.deleteById(id);
    }
}
