package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.model.EstadoCita;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaService {

    @Autowired
    private EmailService emailService;
    private final CitaRepository citaRepository;

    private static final List<String> HORAS_POSIBLES = List.of(
        "09:00:00", "10:00:00", "11:00:00",
        "13:00:00", "14:00:00", "15:00:00", "16:00:00"
    );

    public List<String> obtenerHorasDisponibles(LocalDate fecha) {
        List<String> horasReservadas = citaRepository.findHorasReservadas(fecha);

        List<String> disponibles = HORAS_POSIBLES.stream()
            .filter(hora -> !horasReservadas.contains(hora))
            .collect(Collectors.toList());

        return disponibles;
    }

    public List<Cita> listarPorUsuarioYFecha(LocalDate fecha, Long usuarioId) {
        LocalDateTime start = fecha.atStartOfDay();
        LocalDateTime end = fecha.plusDays(1).atStartOfDay();
        return citaRepository.findByUsuarioIdAndFecha(usuarioId, start, end);
    }

    public Cita registrar(Cita cita) {
        citaRepository.save(cita); 
        emailService.enviarCorreoCita(cita); 
        return cita;
    }

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> listarPorUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    public void cancelar(Long id) {
        citaRepository.findById(id).ifPresent(cita -> {
            cita.setEstado(EstadoCita.CANCELADA);
            citaRepository.save(cita);
        });
    }

    public Cita actualizar(Long id, Cita data) {
        return citaRepository.findById(id).map(cita -> {
            cita.setFechaHora(data.getFechaHora());
            cita.setDuracion(data.getDuracion());
            cita.setServicio(data.getServicio());
            cita.setEstado(data.getEstado());
            cita.setNotas(data.getNotas());
            return citaRepository.save(cita);
        }).orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public void eliminar(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la cita con id: " + id);
        }
        citaRepository.deleteById(id);
    }

    public Optional<String> obtenerEmailUsuarioPorCitaId(Long citaId) {
        return citaRepository.findById(citaId)
                .map(Cita::getUsuario)
                .map(Usuario::getEmail);
    }
}