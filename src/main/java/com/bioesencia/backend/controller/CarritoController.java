package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.CarritoItem;
import com.bioesencia.backend.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<CarritoItem>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.listarPorUsuario(usuarioId));
    }

    @PostMapping("/agregar")
    public ResponseEntity<CarritoItem> agregarItem(@RequestParam Long usuarioId,
                                                   @RequestParam Long productoId,
                                                   @RequestParam int cantidad) {
        CarritoItem nuevo = carritoService.agregarItem(usuarioId, productoId, cantidad);
        return ResponseEntity.ok(nuevo);
    }

    @DeleteMapping("/eliminar/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long itemId) {
        carritoService.eliminarItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/limpiar/{usuarioId}")
    public ResponseEntity<Void> limpiarCarrito(@PathVariable Long usuarioId) {
        carritoService.limpiarPorUsuario(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
