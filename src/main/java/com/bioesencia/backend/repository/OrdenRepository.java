package com.bioesencia.backend.repository;

import com.bioesencia.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByUsuarioId(Long usuarioId);
    Optional<Orden> findByCodigoOrden(String codigoOrden);
}
