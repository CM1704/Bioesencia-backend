package com.bioesencia.backend.dto;

import java.time.LocalDateTime;

public record InscripcionTallerResumenDTO(
    Long id,
    LocalDateTime fechaInscripcion,
    String estado,
    Long tallerId,
    String nombreTaller,
    Long usuarioId,
    String nombreUsuario,
    String emailUsuario
) {}