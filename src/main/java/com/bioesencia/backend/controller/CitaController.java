package com.bioesencia.backend.controller;

import com.bioesencia.backend.dto.CitaDTO;
import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.service.CitaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<Cita> registrar(@RequestBody CitaDTO dto) {
        Cita cita = new Cita();
        cita.setCorreo(dto.getCorreo());
        cita.setDuracion(dto.getDuracion());
        cita.setEstado(dto.getEstado());
        cita.setFechaHora(dto.getFechaHora());
        cita.setNotas(dto.getNotas());
        cita.setServicio(dto.getServicio());

        // Cita citaGuardada = citaService.registrar(cita, dto.getUsuarioId());
        return ResponseEntity.status(201).body(citaService.registrar(cita, dto.getUsuarioId()));
    }

    @GetMapping
    public List<CitaDTO> listarTodos() {
        List<Cita> citas = citaService.findAll();

        return citas.stream().map(cita -> {
            CitaDTO dto = new CitaDTO();
            dto.setCorreo(cita.getCorreo());
            dto.setDuracion(cita.getDuracion());
            dto.setEstado(cita.getEstado());
            dto.setFechaHora(cita.getFechaHora());
            dto.setNotas(cita.getNotas());
            dto.setServicio(cita.getServicio());

            if (cita.getUsuario() != null) {
                dto.setUsuarioId(cita.getUsuario().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        return citaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.listarPorUsuario(usuarioId));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}