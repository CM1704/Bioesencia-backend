package com.bioesencia.backend.controller;

import com.bioesencia.backend.dto.InscripcionTallerResumenDTO;
import com.bioesencia.backend.model.EstadoInscripcion;
import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.service.InscripcionTallerService;
import lombok.RequiredArgsConstructor;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionTallerController {

    private final InscripcionTallerService inscripcionService;

    @GetMapping("/agendadas/{fecha}/{usuarioId}")
    public List<Taller> inscripcionesAgendadas(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha, @PathVariable Long usuarioId) {
        return inscripcionService.listarPorUsuarioYFecha(usuarioId, fecha);
    }

    @PostMapping
    public ResponseEntity<InscripcionTaller> registrar(@RequestBody InscripcionTaller inscripcion) {
        return ResponseEntity.status(201).body(inscripcionService.registrar(inscripcion));
    }


    @GetMapping
    public ResponseEntity<List<InscripcionTaller>> listar() {
        return ResponseEntity.ok(inscripcionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionTaller> buscarPorId(@PathVariable Long id) {
        return inscripcionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InscripcionTaller>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(inscripcionService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<InscripcionTaller>> listarPorTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(inscripcionService.listarPorTaller(tallerId));
    }

    @GetMapping("/resumen")
    public List<InscripcionTallerResumenDTO> resumen(
        @RequestParam(required = false) EstadoInscripcion estado,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return inscripcionService.listarResumenPorEstadoYFecha(estado, fecha);
    }

    @GetMapping("/resumen/taller/{tallerId}")
    public List<InscripcionTallerResumenDTO> resumenPorTaller(@PathVariable Long tallerId) {
        return inscripcionService.listarResumenPorTaller(tallerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inscripcionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
