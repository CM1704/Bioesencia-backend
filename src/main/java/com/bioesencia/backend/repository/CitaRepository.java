package com.bioesencia.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bioesencia.backend.model.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByUsuarioId(Long usuarioId);

    @Query(value = "SELECT TIME(c.fecha_hora) FROM citas c WHERE DATE(c.fecha_hora) = :fecha", nativeQuery = true)
    List<String> findHorasReservadas(@Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Cita c WHERE c.usuario.id = :usuarioId AND c.fechaHora >= :start AND c.fechaHora < :end")
    List<Cita> findByUsuarioIdAndFecha(@Param("usuarioId") Long usuarioId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}

