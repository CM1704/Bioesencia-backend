package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;

    public Cita registrar(Cita cita) {
        return citaRepository.save(cita);
    }

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public Optional<Cita> buscarPorId(Long id) {
        return citaRepository.findById(id);
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
