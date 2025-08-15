package com.bioesencia.backend.repository;

import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.model.Orden;
import com.bioesencia.backend.model.Taller;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InscripcionTallerRepository extends JpaRepository<InscripcionTaller, Long> {
    @Query("SELECT i.taller FROM InscripcionTaller i WHERE i.usuario.id = :usuarioId AND i.fechaInscripcion >= :start AND i.fechaInscripcion < :end")
    List<Taller> findTalleresByUsuarioIdAndFecha(@Param("usuarioId") Long usuarioId, @Param("start") LocalDateTime start,@Param("end") LocalDateTime end);

    @Override
    @EntityGraph(attributePaths = {"taller", "usuario"})
    Optional<InscripcionTaller> findById(Long id);
                        
    @Override
    @EntityGraph(attributePaths = {"taller", "usuario"})
    List<InscripcionTaller> findAll();

    @EntityGraph(attributePaths = "taller")
    List<InscripcionTaller> findByTallerId(Long tallerId);

    @EntityGraph(attributePaths = "usuario")
    List<InscripcionTaller> findByUsuarioId(Long usuarioId);
}