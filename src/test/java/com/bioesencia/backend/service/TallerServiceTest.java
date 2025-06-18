package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.repository.TallerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TallerServiceTest {

    @Mock
    private TallerRepository tallerRepository;

    @InjectMocks
    private TallerService tallerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Taller crearTallerDummy() {
        return Taller.builder()
                .id(1L)
                .titulo("Meditación Guiada")
                .descripcion("Sesión de meditación")
                .fechaInicio(LocalDateTime.now())
                .fechaFin(LocalDateTime.now().plusHours(2))
                .lugar("San José")
                .cupoMaximo(30)
                .precio(BigDecimal.ZERO)
                .activo(true)
                .build();
    }

    @Test
    void testRegistrarTaller() {
        Taller taller = crearTallerDummy();
        when(tallerRepository.save(any(Taller.class))).thenReturn(taller);

        Taller resultado = tallerService.registrar(taller);

        assertNotNull(resultado);
        assertEquals("Meditación Guiada", resultado.getTitulo());
        verify(tallerRepository, times(1)).save(any(Taller.class));
    }

    @Test
    void testListarTalleres() {
        when(tallerRepository.findAll()).thenReturn(List.of(crearTallerDummy()));

        List<Taller> lista = tallerService.listar();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void testBuscarPorId() {
        Taller taller = crearTallerDummy();
        when(tallerRepository.findById(1L)).thenReturn(Optional.of(taller));

        Optional<Taller> encontrado = tallerService.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("Meditación Guiada", encontrado.get().getTitulo());
    }

    @Test
    void testBuscarPorIdInexistente() {
        when(tallerRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Taller> resultado = tallerService.buscarPorId(999L);

        assertTrue(resultado.isEmpty());
    }
}
