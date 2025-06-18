package com.bioesencia.backend.service;

import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.repository.InscripcionTallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscripcionTallerService {

    private final InscripcionTallerRepository inscripcionRepo;

    public InscripcionTaller registrar(InscripcionTaller inscripcion) {
        inscripcion.setFechaInscripcion(LocalDateTime.now());
        return inscripcionRepo.save(inscripcion);
    }

    public List<InscripcionTaller> listar() {
        return inscripcionRepo.findAll();
    }

    public Optional<InscripcionTaller> buscarPorId(Long id) {
        return inscripcionRepo.findById(id);
    }

    public List<InscripcionTaller> listarPorUsuario(Long usuarioId) {
        return inscripcionRepo.findByUsuarioId(usuarioId);
    }

    public List<InscripcionTaller> listarPorTaller(Long tallerId) {
        return inscripcionRepo.findByTallerId(tallerId);
    }
}
