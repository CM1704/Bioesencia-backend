package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.CitaRepository;
import com.bioesencia.backend.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitaService {

    @Autowired 
    private EmailService emailService;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Cita registrar(Cita cita, Long usuarioId) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            cita.setUsuario(usuario);
            Cita nuevaCita = citaRepository.save(cita); // <- Si falla aquí, se lanza una excepción
            emailService.enviarCorreoCita(nuevaCita.getUsuario().getEmail(), nuevaCita); // Aquí puedes pasar los parámetros necesarios
            return nuevaCita;
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Cita> findAll() {
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