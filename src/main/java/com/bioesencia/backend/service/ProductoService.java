package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Producto;
import com.bioesencia.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Producto registrar(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Optional<Producto> actualizar(Long id, Producto producto) {
        return productoRepository.findById(id).map(existing -> {
            existing.setNombre(producto.getNombre());
            existing.setDescripcion(producto.getDescripcion());
            existing.setPrecio(producto.getPrecio());
            existing.setStock(producto.getStock());
            existing.setImagenUrl(producto.getImagenUrl());
            existing.setActivo(producto.getActivo());
            return productoRepository.save(existing);
        });
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
