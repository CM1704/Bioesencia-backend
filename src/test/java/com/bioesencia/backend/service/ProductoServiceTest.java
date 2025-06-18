package com.bioesencia.backend.service;

import com.bioesencia.backend.model.Producto;
import com.bioesencia.backend.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Producto crearProductoDummy() {
        return Producto.builder()
                .nombre("Aceite esencial")
                .descripcion("Relajante")
                .precio(new BigDecimal("4500"))
                .stock(100)
                .imagenUrl("http://ejemplo.com/aceite.jpg")
                .build();
    }

    @Test
    void testRegistrarProducto() {
        Producto producto = crearProductoDummy();
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto guardado = productoService.registrar(producto);

        assertNotNull(guardado);
        assertEquals("Aceite esencial", guardado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testListarProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(crearProductoDummy()));

        List<Producto> productos = productoService.listar();

        assertFalse(productos.isEmpty());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Producto producto = crearProductoDummy();
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> encontrado = productoService.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals("Aceite esencial", encontrado.get().getNombre());
    }

    @Test
    void testEliminarProducto() {
        Long id = 1L;

        productoService.eliminar(id);

        verify(productoRepository, times(1)).deleteById(id);
    }
}
