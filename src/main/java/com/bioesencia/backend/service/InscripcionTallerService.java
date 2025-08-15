package com.bioesencia.backend.service;

import com.bioesencia.backend.model.EstadoInscripcion;
import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.repository.InscripcionTallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscripcionTallerService {

    private final InscripcionTallerRepository inscripcionRepository;

    public InscripcionTaller registrar(InscripcionTaller inscripcion) {
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        inscripcion.setEstado(EstadoInscripcion.PENDIENTE);
        return inscripcionRepository.save(inscripcion);
    }

    public List<InscripcionTaller> listar() {
        return inscripcionRepository.findAll();
    }

    public void eliminar(Long id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Inscripción no encontrada");
        }
        inscripcionRepository.deleteById(id);
    }

    public Optional<InscripcionTaller> buscarPorId(Long id) {
        return inscripcionRepository.findById(id);
    }

    public List<Taller> listarPorUsuarioYFecha(Long usuarioId, LocalDate fecha) {
        LocalDateTime start = fecha.atStartOfDay();
        LocalDateTime end = fecha.plusDays(1).atStartOfDay();
        return inscripcionRepository.findTalleresByUsuarioIdAndFecha(usuarioId, start, end);
    }

    public List<InscripcionTaller> listarPorUsuario(Long usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId);
    }

    public List<InscripcionTaller> listarPorTaller(Long tallerId) {
        return inscripcionRepository.findByTallerId(tallerId);
    }

}
