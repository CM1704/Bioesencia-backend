package com.bioesencia.backend.repository;

import com.bioesencia.backend.dto.InscripcionTallerResumenDTO;
import com.bioesencia.backend.model.InscripcionTaller;
import com.bioesencia.backend.model.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InscripcionTallerRepository extends JpaRepository<InscripcionTaller, Long> {
    List<InscripcionTaller> findByUsuarioId(Long usuarioId);
    List<InscripcionTaller> findByTallerId(Long tallerId);

    @Query("SELECT i.taller FROM InscripcionTaller i WHERE i.usuario.id = :usuarioId AND i.fechaInscripcion >= :start AND i.fechaInscripcion < :end")
    List<Taller> findTalleresByUsuarioIdAndFecha(@Param("usuarioId") Long usuarioId, @Param("start") LocalDateTime start,@Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.bioesencia.backend.dto.InscripcionTallerResumenDTO(
            i.id,
            i.fechaInscripcion,
            CAST(i.estado AS string),
            i.taller.id,
            i.taller.titulo,
            i.usuario.id,
            i.usuario.nombre,
            i.usuario.email
        )
        FROM InscripcionTaller i
        WHERE (:estado IS NULL OR i.estado = :estado)
          AND (:fecha IS NULL OR DATE(i.fechaInscripcion) = :fecha)
    """)
    List<InscripcionTallerResumenDTO> findResumenByEstadoAndFecha(
        @Param("estado") com.bioesencia.backend.model.EstadoInscripcion estado,
        @Param("fecha") java.time.LocalDate fecha
    );

    @Query("""
        SELECT new com.bioesencia.backend.dto.InscripcionTallerResumenDTO(
            i.id,
            i.fechaInscripcion,
            CAST(i.estado AS string),
            i.taller.id,
            i.taller.titulo,
            i.usuario.id,
            i.usuario.nombre,
            i.usuario.email
        )
        FROM InscripcionTaller i
        WHERE i.taller.id = :tallerId
    """)
    List<InscripcionTallerResumenDTO> findResumenByTallerId(@Param("tallerId") Long tallerId);
}