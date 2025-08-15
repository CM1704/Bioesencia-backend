package com.bioesencia.backend.repository;

import com.bioesencia.backend.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    @Query("SELECT c FROM CarritoItem c JOIN FETCH c.producto p WHERE c.usuario.id = :usuarioId AND p.activo = true")
    List<CarritoItem> findByUsuarioIdAndProductoActivo(Long usuarioId);

    void deleteByUsuarioId(Long usuarioId);

    @Query("SELECT c FROM CarritoItem c WHERE c.usuario.id = :usuarioId AND c.producto.id = :productoId")
    Optional<CarritoItem> findByUsuarioIdAndProductoId(Long usuarioId, Long productoId);
}

