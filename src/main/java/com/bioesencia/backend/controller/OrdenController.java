package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.EstadoOrden;
import com.bioesencia.backend.model.Orden;
import com.bioesencia.backend.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;

    @PostMapping
    public ResponseEntity<Orden> registrar(@RequestBody Orden orden) {
        Orden creada = ordenService.registrar(orden);
        return ResponseEntity.status(201).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Orden>> listar() {
        return ResponseEntity.ok(ordenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> buscarPorId(@PathVariable Long id) {
        Orden orden = ordenService.buscarPorId(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Orden>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ordenService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/codigo/{codigoOrden}")
    public ResponseEntity<Orden> buscarPorCodigo(@PathVariable String codigoOrden) {
        Orden orden = ordenService.buscarPorCodigo(codigoOrden);
        return ResponseEntity.ok(orden);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Orden> actualizarEstado(
            @PathVariable Long id,
            @RequestParam("estado") EstadoOrden estado
    ) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, estado));
    }
}
