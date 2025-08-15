package com.bioesencia.backend.service;

import com.bioesencia.backend.model.CarritoItem;
import com.bioesencia.backend.model.Producto;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.CarritoItemRepository;
import com.bioesencia.backend.repository.ProductoRepository;
import com.bioesencia.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoItem agregarItem(Long usuarioId, Long productoId, int cantidad) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Producto producto = productoRepository.findById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        // Verifica si ya existe el ítem con ese producto para ese usuario
        return carritoItemRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
        .map(item -> {
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoItemRepository.save(item);
        })
        .orElseGet(() -> {
            CarritoItem nuevo = CarritoItem.builder()
            .usuario(usuario)
            .producto(producto)
            .cantidad(cantidad)
            .build();
            return carritoItemRepository.save(nuevo);
        });
    }

    public List<CarritoItem> listarPorUsuario(Long usuarioId) {
        return carritoItemRepository.findByUsuarioIdAndProductoActivo(usuarioId);
    }
    
    public void eliminarItem(Long itemId) {
        carritoItemRepository.deleteById(itemId);
    }

    public void limpiarPorUsuario(Long usuarioId) {
        carritoItemRepository.deleteByUsuarioId(usuarioId);
    }
}
