package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Orden;
import com.bioesencia.backend.repository.OrdenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;

    public Orden registrar(Orden orden) {
        orden.setFechaOrden(LocalDateTime.now());
        return ordenRepository.save(orden);
    }

    public List<Orden> listar() {
        return ordenRepository.findAll();
    }

    public Optional<Orden> buscarPorId(Long id) {
        return ordenRepository.findById(id);
    }

    public List<Orden> listarPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }
}
