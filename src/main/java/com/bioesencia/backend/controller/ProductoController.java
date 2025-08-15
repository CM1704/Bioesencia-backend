package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.Producto;
import com.bioesencia.backend.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto creado = productoService.registrar(producto);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Optional<Producto> actualizado = productoService.actualizar(id, producto);
        return actualizado.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        Optional<Producto> optProducto = productoService.buscarPorId(id);
        return optProducto.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        List<Producto> activos = productoService.listarActivos();
        return ResponseEntity.ok(activos);
    }
}
