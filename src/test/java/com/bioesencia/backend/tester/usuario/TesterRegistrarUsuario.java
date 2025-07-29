package com.bioesencia.backend.tester.usuario;

import com.bioesencia.backend.model.*;

import com.bioesencia.backend.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

// Agregado por Joseph Gray
import java.util.UUID;
// 

@TestPropertySource("classpath:application.properties")
@SpringBootTest
public class TesterRegistrarUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void registrarUsuario() {
        Usuario usuario = Usuario.builder()
                .nombre("Juan")
                .apellido("Perez")
                // Agregado por Joseph Gray
                // Genera un email único para evitar duplicados
                .email("juan"+ UUID.randomUUID().toString().replace("-", "").substring(0, 8) +"@bio.com")
                // 
                .password("clave1234")
                .telefono("8888-0000")
                .fechaRegistro(LocalDateTime.now())
                .rol(RolUsuario.CLIENTE)
                .activo(true)
                .build();

        Usuario guardado = usuarioService.registrar(usuario);
        System.out.println("✅ Password en DB: " + guardado.getPassword());
    }
}
