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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitaService {

    @Autowired
    private EmailService emailService;
    private final CitaRepository citaRepository;

    public Cita registrar(Cita cita) {
        citaRepository.save(cita); 
        emailService.enviarCorreoCita(cita.getUsuario().getEmail(), cita); 
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