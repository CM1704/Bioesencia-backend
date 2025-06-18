package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Taller;
import com.bioesencia.backend.service.TallerService;
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
    public ResponseEntity<Taller> registrar(@RequestBody Taller taller) {
        Taller creado = tallerService.registrar(taller);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<Taller>> listar() {
        return ResponseEntity.ok(tallerService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taller> buscarPorId(@PathVariable Long id) {
        return tallerService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
