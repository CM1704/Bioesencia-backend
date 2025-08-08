package com.bioesencia.backend.repository;

import com.bioesencia.backend.model.Orden;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenRepository extends JpaRepository<Orden, Long> {

    @Override
    @EntityGraph(attributePaths = "usuario")
    List<Orden> findAll();

    @EntityGraph(attributePaths = "usuario")
    List<Orden> findByUsuarioId(Long usuarioId);

    @EntityGraph(attributePaths = "usuario")
    Optional<Orden> findByCodigoOrden(String codigoOrden);

    @Override
    @EntityGraph(attributePaths = "usuario")
    Optional<Orden> findById(Long id);
}