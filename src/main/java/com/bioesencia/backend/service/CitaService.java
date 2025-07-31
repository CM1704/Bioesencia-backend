package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.model.EstadoCita;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.CitaRepository;
import com.bioesencia.backend.repository.UsuarioRepository;
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
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Cita registrar(Cita cita) {
        if (cita.getUsuario() != null && cita.getUsuario().getId() != null) {
            Usuario usuarioCompleto = usuarioRepository.findById(cita.getUsuario().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para la cita"));
            cita.setUsuario(usuarioCompleto);
        } else {
            throw new RuntimeException("No se ha asignado un usuario a la cita");
        }
        citaRepository.save(cita);
        emailService.enviarCorreoCita(cita.getUsuario().getEmail(), cita);
        return cita;
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