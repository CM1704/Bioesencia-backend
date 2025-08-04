package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.service.CitaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping("/horariosDisponibles")
    public ResponseEntity<List<String>> getHorasDisponibles(@RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<String> disponibles = citaService.obtenerHorasDisponibles(fecha);
        return ResponseEntity.ok(disponibles);
    }
    
    @PostMapping
    public ResponseEntity<Cita> registrar(@RequestBody Cita cita) {
        return ResponseEntity.status(201).body(citaService.registrar(cita));
    }
    
    @GetMapping
    public List<Cita> listarTodos() {
        return citaService.findAll(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @Valid @RequestBody Cita cita) {
        return citaService.findById(id)
                .map(actual -> {
                    cita.setId(id);
                    Cita actualizado = citaService.registrar(cita);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return citaService.findById(id)
                .map(s -> {
                    citaService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
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