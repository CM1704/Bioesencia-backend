package com.bioesencia.backend.tester.usuario;

import com.bioesencia.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TesterEliminarUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void eliminarUsuario() {
        Long id = 1L;
        usuarioRepository.deleteById(id);
        System.out.println("🗑 Usuario eliminado con ID: " + id);
    }
}
