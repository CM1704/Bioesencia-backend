package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductoRepository productoRepository;
    private final PdfGeneratorService pdfGeneratorService;
    private final EmailService emailService;
    private final CarritoItemRepository carritoItemRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Orden registrar(Orden orden) {
        // Cargar usuario completo desde BD
        Usuario usuarioCompleto = usuarioRepository.findById(orden.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        orden.setUsuario(usuarioCompleto);

        orden.setFechaOrden(LocalDateTime.now());
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setCodigoOrden(generarCodigoOrdenUnico());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem item : orden.getItems()) {
            Producto producto = productoRepository.findById(item.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Actualizar stock y estado del producto
            producto.setStock(producto.getStock() - item.getCantidad());
            if (producto.getStock() == 0) {
                producto.setActivo(false);
            }
            productoRepository.save(producto);

            item.setOrden(orden);
            item.setProducto(producto);
            item.setPrecioUnitario(producto.getPrecio());

            BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());
            subtotal = subtotal.add(producto.getPrecio().multiply(cantidad));
        }

        BigDecimal impuesto = subtotal.multiply(BigDecimal.valueOf(0.13)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(impuesto).setScale(2, RoundingMode.HALF_UP);
        orden.setTotal(total);

        Orden ordenGuardada = ordenRepository.save(orden);
        orderItemRepository.saveAll(orden.getItems());

        // Vaciar el carrito
        carritoItemRepository.deleteByUsuarioId(usuarioCompleto.getId());

        try {
            if (usuarioCompleto.getEmail() != null && !usuarioCompleto.getEmail().isBlank()) {
                byte[] pdf = pdfGeneratorService.generarPdfOrden(ordenGuardada);
                emailService.enviarCorreoConAdjunto(usuarioCompleto.getEmail(), ordenGuardada.getCodigoOrden(), pdf);
            } else {
                System.out.println("❌ El usuario no tiene correo, no se puede enviar PDF.");
            }
        } catch (Exception e) {
            System.err.println("Error al generar o enviar el PDF: " + e.getMessage());
        }

        return ordenGuardada;
    }

    public List<Orden> listar() {
        return ordenRepository.findAll();
    }

    @Transactional
    public Orden actualizarEstado(Long id, EstadoOrden nuevoEstado) {
        Orden orden = ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        orden.setEstado(nuevoEstado);
        return ordenRepository.save(orden);
    }

    public Orden buscarPorId(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    public List<Orden> listarPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioId(usuarioId);
    }

    public Orden buscarPorCodigo(String codigo) {
        return ordenRepository.findByCodigoOrden(codigo)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }

    private String generarCodigoOrdenUnico() {
        return "BO-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
