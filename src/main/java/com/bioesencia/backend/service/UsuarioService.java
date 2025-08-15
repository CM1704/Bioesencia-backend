package com.bioesencia.backend.service;

import com.bioesencia.backend.model.RolUsuario;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public Usuario registrar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol(RolUsuario.CLIENTE);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarPorRol(String rol) {
        try {
            return usuarioRepository.findByRol(com.bioesencia.backend.model.RolUsuario.valueOf(rol));
        } catch (Exception e) {
            throw new IllegalArgumentException("Rol no válido: " + rol);
        }
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}