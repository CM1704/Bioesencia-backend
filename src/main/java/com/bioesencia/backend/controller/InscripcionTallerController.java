package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.service.InscripcionTallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionTallerController {

    private final InscripcionTallerService inscripcionService;

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
}
