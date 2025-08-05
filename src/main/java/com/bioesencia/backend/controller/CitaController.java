package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.service.CitaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.actualizar(id, cita));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        citaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/usuario-email")
    public ResponseEntity<Map<String, String>> getUsuarioEmailByCita(@PathVariable Long id) {
        return citaService.findById(id)
                .map(cita -> {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", cita.getUsuario() != null ? cita.getUsuario().getEmail() : "");
                    return ResponseEntity.ok(data);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
