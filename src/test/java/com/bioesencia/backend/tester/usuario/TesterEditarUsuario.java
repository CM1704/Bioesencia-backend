package com.bioesencia.backend.tester.usuario;

import com.bioesencia.backend.model.RolUsuario;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@SpringBootTest
public class TesterEditarUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void editarUsuario() {
        Usuario u = usuarioRepository.findById(1L).orElseThrow();
        u.setNombre("Ana Laura");
        u.setActivo(false);
        usuarioRepository.save(u);
        System.out.println("✔ Usuario actualizado: " + u.getNombre());
    }
}
