package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrdenServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdenService ordenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario usuarioDummy() {
        return Usuario.builder()
                .id(1L)
                .nombre("Cliente")
                .email("cliente@bio.com")
                .password("1234")
                .rol(RolUsuario.CLIENTE)
                .build();
    }

    private Orden crearOrdenDummy() {
        return Orden.builder()
                .id(1L)
                .usuario(usuarioDummy())
                .fechaOrden(LocalDateTime.now())
                .total(new BigDecimal("10000"))
                .estado(EstadoOrden.PENDIENTE)
                .build();
    }

    @Test
    void testRegistrarOrden() {
        Orden orden = crearOrdenDummy();
        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden resultado = ordenService.registrar(orden);

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(10000), resultado.getTotal());
        verify(ordenRepository, times(1)).save(any(Orden.class));
    }

    @Test
    void testListarOrdenes() {
        when(ordenRepository.findAll()).thenReturn(List.of(crearOrdenDummy()));

        List<Orden> ordenes = ordenService.listar();

        assertFalse(ordenes.isEmpty());
        verify(ordenRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Orden orden = crearOrdenDummy();
        when(ordenRepository.findById(1L)).thenReturn(Optional.of(orden));

        Optional<Orden> encontrado = ordenService.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals(1L, encontrado.get().getId());
    }

    @Test
    void testListarPorUsuario() {
        when(ordenRepository.findByUsuarioId(1L)).thenReturn(List.of(crearOrdenDummy()));

        List<Orden> ordenes = ordenService.listarPorUsuario(1L);

        assertEquals(1, ordenes.size());
        assertEquals(1L, ordenes.get(0).getUsuario().getId());
    }
}
