package com.bioesencia.backend.repository;

import com.bioesencia.backend.model.InscripcionTaller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscripcionTallerRepository extends JpaRepository<InscripcionTaller, Long> {
    List<InscripcionTaller> findByUsuarioId(Long usuarioId);
    List<InscripcionTaller> findByTallerId(Long tallerId);
}
