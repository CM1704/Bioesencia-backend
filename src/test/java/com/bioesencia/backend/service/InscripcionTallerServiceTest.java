package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.InscripcionTallerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InscripcionTallerServiceTest {

    @Mock
    private InscripcionTallerRepository inscripcionRepo;

    @InjectMocks
    private InscripcionTallerService inscripcionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario usuarioDummy() {
        return Usuario.builder()
                .id(1L)
                .nombre("Lucía")
                .email("lucia@bio.com")
                .build();
    }

    private Taller tallerDummy() {
        return Taller.builder()
                .id(2L)
                .titulo("Yoga Energético")
                .build();
    }

    private InscripcionTaller crearInscripcionDummy() {
        return InscripcionTaller.builder()
                .id(10L)
                .usuario(usuarioDummy())
                .taller(tallerDummy())
                .fechaInscripcion(LocalDateTime.now())
                .estado(EstadoInscripcion.CONFIRMADA)
                .build();
    }

    @Test
    void testRegistrarInscripcion() {
        InscripcionTaller inscripcion = crearInscripcionDummy();
        when(inscripcionRepo.save(any(InscripcionTaller.class))).thenReturn(inscripcion);

        InscripcionTaller resultado = inscripcionService.registrar(inscripcion);

        assertNotNull(resultado);
        assertEquals("Yoga Energético", resultado.getTaller().getTitulo());
    }

    @Test
    void testListarInscripciones() {
        when(inscripcionRepo.findAll()).thenReturn(List.of(crearInscripcionDummy()));

        List<InscripcionTaller> lista = inscripcionService.listar();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void testListarPorUsuario() {
        when(inscripcionRepo.findByUsuarioId(1L)).thenReturn(List.of(crearInscripcionDummy()));

        List<InscripcionTaller> lista = inscripcionService.listarPorUsuario(1L);

        assertEquals(1, lista.size());
        assertEquals(1L, lista.get(0).getUsuario().getId());
    }

    @Test
    void testListarPorTaller() {
        when(inscripcionRepo.findByTallerId(2L)).thenReturn(List.of(crearInscripcionDummy()));

        List<InscripcionTaller> lista = inscripcionService.listarPorTaller(2L);

        assertEquals(1, lista.size());
        assertEquals(2L, lista.get(0).getTaller().getId());
    }
}
