package com.bioesencia.backend.service;

import com.bioesencia.backend.model.RolUsuario;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario crearUsuarioDummy() {
        return Usuario.builder()
                .nombre("Carlos")
                .apellido("Marín")
                .email("carlos@test.com")
                .password("1234")
                .rol(RolUsuario.CLIENTE)
                .build();
    }

    @Test
    void testRegistrarUsuario() {
        Usuario usuario = crearUsuarioDummy();
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.registrar(usuario);

        assertNotNull(resultado);
        assertEquals("Carlos", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testListarUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(crearUsuarioDummy()));
        List<Usuario> lista = usuarioService.listar();

        assertFalse(lista.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Usuario usuario = crearUsuarioDummy();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> encontrado = usuarioService.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("carlos@test.com", encontrado.get().getEmail());
    }

    @Test
    void testBuscarPorEmail() {
        Usuario usuario = crearUsuarioDummy();
        when(usuarioRepository.findByEmail("carlos@test.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> encontrado = usuarioService.buscarPorEmail("carlos@test.com");

        assertTrue(encontrado.isPresent());
        assertEquals("Carlos", encontrado.get().getNombre());
    }
}
