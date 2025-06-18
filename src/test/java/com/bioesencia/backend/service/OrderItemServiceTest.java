package com.bioesencia.backend.service;

import com.bioesencia.backend.model.*;
import com.bioesencia.backend.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Producto productoDummy() {
        return Producto.builder()
                .id(10L)
                .nombre("Aceite")
                .precio(new BigDecimal("3000"))
                .stock(50)
                .build();
    }

    private Orden ordenDummy() {
        return Orden.builder()
                .id(20L)
                .total(new BigDecimal("9000"))
                .estado(EstadoOrden.PAGADO)
                .build();
    }

    private OrderItem crearItemDummy() {
        return OrderItem.builder()
                .id(1L)
                .producto(productoDummy())
                .orden(ordenDummy())
                .cantidad(3)
                .precioUnitario(new BigDecimal("3000"))
                .build();
    }

    @Test
    void testRegistrarOrderItem() {
        OrderItem item = crearItemDummy();
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(item);

        OrderItem resultado = orderItemService.registrar(item);

        assertNotNull(resultado);
        assertEquals(3, resultado.getCantidad());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testListarOrderItems() {
        when(orderItemRepository.findAll()).thenReturn(List.of(crearItemDummy()));

        List<OrderItem> items = orderItemService.listar();

        assertFalse(items.isEmpty());
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(crearItemDummy()));

        Optional<OrderItem> encontrado = orderItemService.buscarPorId(1L);

        assertTrue(encontrado.isPresent());
        assertEquals(3, encontrado.get().getCantidad());
    }

    @Test
    void testListarPorOrden() {
        when(orderItemRepository.findByOrdenId(20L)).thenReturn(List.of(crearItemDummy()));

        List<OrderItem> items = orderItemService.listarPorOrden(20L);

        assertEquals(1, items.size());
        assertEquals(3, items.get(0).getCantidad());
    }
}
