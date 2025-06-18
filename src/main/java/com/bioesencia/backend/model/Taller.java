package com.bioesencia.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "talleres")
public class Taller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @Lob
    @NotBlank
    private String descripcion;

    @NotNull
    private LocalDateTime fechaInicio;

    @NotNull
    private LocalDateTime fechaFin;

    @NotBlank
    private String lugar;

    @NotNull
    private Integer cupoMaximo;

    private BigDecimal precio = BigDecimal.ZERO;

    private boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL)
    private List<InscripcionTaller> inscripciones;
}
