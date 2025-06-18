package com.bioesencia.backend.tester.usuario;

import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class TesterListarUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void listarUsuarios() {
        List<Usuario> lista = usuarioRepository.findAll();
        System.out.println("📋 Usuarios registrados:");
        lista.forEach(u -> System.out.println(u.getId() + " - " + u.getNombre()));
    }
}
