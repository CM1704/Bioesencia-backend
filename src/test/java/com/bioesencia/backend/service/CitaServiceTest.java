package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaService citaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Usuario usuarioDummy() {
        return Usuario.builder()
                .id(1L)
                .nombre("Marta")
                .email("marta@bio.com")
                .build();
    }

    private Cita crearCitaDummy() {
        return Cita.builder()
                .id(5L)
                .usuario(usuarioDummy())
                .fechaHora(LocalDateTime.now().plusDays(1))
                .duracion(60)
                .servicio("Terapia Reiki")
                .estado(EstadoCita.AGENDADA)
                .notas("Primera sesión")
                .build();
    }

    @Test
    void testRegistrarCita() {
        Cita cita = crearCitaDummy();
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.registrar(cita);

        assertNotNull(resultado);
        assertEquals("Terapia Reiki", resultado.getServicio());
    }

    @Test
    void testListarCitas() {
        when(citaRepository.findAll()).thenReturn(List.of(crearCitaDummy()));

        List<Cita> citas = citaService.listar();

        assertEquals(1, citas.size());
        assertEquals(60, citas.get(0).getDuracion());
    }

    @Test
    void testBuscarPorId() {
        when(citaRepository.findById(5L)).thenReturn(Optional.of(crearCitaDummy()));

        Optional<Cita> cita = citaService.buscarPorId(5L);

        assertTrue(cita.isPresent());
        assertEquals("Terapia Reiki", cita.get().getServicio());
    }

    @Test
    void testCancelarCita() {
        Cita cita = crearCitaDummy();
        when(citaRepository.findById(5L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        citaService.cancelar(5L);

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        verify(citaRepository, times(1)).save(cita);
    }
}
