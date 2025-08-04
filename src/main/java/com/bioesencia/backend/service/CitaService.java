package com.bioesencia.backend.service;

import com.bioesencia.backend.dto.CitaDTO;
import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.CitaRepository;
import com.bioesencia.backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    public void deleteById(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> listarPorUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    public void cancelar(Long id) {
        citaRepository.findById(id).ifPresent(cita -> {
            cita.setEstado(com.bioesencia.backend.model.EstadoCita.CANCELADA);
            citaRepository.save(cita);
        });
    }
}