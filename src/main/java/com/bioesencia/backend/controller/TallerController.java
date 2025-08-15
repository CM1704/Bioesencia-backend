package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.service.TallerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talleres")
@RequiredArgsConstructor
public class TallerController {

    private final TallerService tallerService;

    @PostMapping
    public ResponseEntity<Taller> crear(@Valid @RequestBody Taller taller) {
        Taller creado = tallerService.save(taller);
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public List<Taller> listarTodos() {
        return tallerService.findAll();
    }

@PutMapping("/{id}")
    public ResponseEntity<Taller> actualizar(@PathVariable Long id, @Valid @RequestBody Taller taller) {
        return tallerService.findById(id)
        .map(actual -> {
            taller.setId(id); // Asegura que el ID es correcto
            Taller actualizado = tallerService.save(taller);
            return ResponseEntity.ok(actualizado);
        })
        .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return tallerService.findById(id)
        .map(t -> {
            tallerService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Taller> obtenerPorId(@PathVariable Long id) {
        return tallerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
