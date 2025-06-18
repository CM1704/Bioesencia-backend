package com.bioesencia.backend.controller;

import com.bioesencia.backend.model.OrderItem;
import com.bioesencia.backend.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    public ResponseEntity<OrderItem> registrar(@RequestBody OrderItem item) {
        OrderItem creado = orderItemService.registrar(item);
        return ResponseEntity.status(201).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<OrderItem>> listar() {
        return ResponseEntity.ok(orderItemService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> buscarPorId(@PathVariable Long id) {
        return orderItemService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<OrderItem>> listarPorOrden(@PathVariable Long ordenId) {
        return ResponseEntity.ok(orderItemService.listarPorOrden(ordenId));
    }
}
