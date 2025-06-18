package com.bioesencia.backend.service;

import com.bioesencia.backend.model.OrderItem;
import com.bioesencia.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItem registrar(OrderItem item) {
        return orderItemRepository.save(item);
    }

    public List<OrderItem> listar() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> buscarPorId(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> listarPorOrden(Long ordenId) {
        return orderItemRepository.findByOrdenId(ordenId);
    }
}
