package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.CitaRepository;
import com.bioesencia.backend.repository.UsuarioRepository;

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

    // Removed duplicate setUp() method

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

    private Long usuarioId = 1L;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @Test
    void testRegistrarCita() {
        Cita cita = crearCitaDummy();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioDummy()));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.registrar(cita);

        assertNotNull(resultado);
        assertEquals("Terapia Reiki", resultado.getServicio());
        verify(emailService, times(1)).enviarCorreoCita(null, cita);
    }

    @Test
    void testRegistrarCitaUsuarioNoExiste() {
        Cita cita = crearCitaDummy();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            citaService.registrar(cita);
        });

        assertEquals("Usuario no encontrado", ex.getMessage());
        verify(emailService, never()).enviarCorreoCita(null, cita);
    }

    @Test
    void testListarCitas() {
        when(citaRepository.findAll()).thenReturn(List.of(crearCitaDummy()));

        List<Cita> citas = citaService.findAll();

        assertEquals(1, citas.size());
        assertEquals(60, citas.get(0).getDuracion());
    }


    @Test
    void testListarPorUsuario() {
        when(citaRepository.findByUsuarioId(usuarioId)).thenReturn(List.of(crearCitaDummy()));

        List<Cita> citas = citaService.listarPorUsuario(usuarioId);

        assertEquals(1, citas.size());
        assertEquals("Terapia Reiki", citas.get(0).getServicio());
    }

    @Test
    void testCancelarCitaExistente() {
        Cita cita = crearCitaDummy();
        when(citaRepository.findById(5L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        citaService.cancelar(5L);

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        verify(citaRepository, times(1)).save(cita);
    }

    @Test
    void testCancelarCitaNoExiste() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        citaService.cancelar(99L);

        verify(citaRepository, never()).save(any(Cita.class));
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
