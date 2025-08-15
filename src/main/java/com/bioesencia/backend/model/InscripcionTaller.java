package com.bioesencia.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inscripciones_taller")
public class InscripcionTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fechaInscripcion = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-inscripcion")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "taller_id", nullable = false)
    @JsonBackReference("taller-inscripcion")
    private Taller taller;

    @JsonProperty("tallerId")
    public Long getTallerIdJson() {
        return (taller != null) ? taller.getId() : null;
    }

    @JsonProperty("tallerNombre")
    public String getTallerNombreJson() {
        return (taller != null) ? taller.getTitulo() : null;
    }

    @JsonProperty("usuarioId")
    public Long getUsuarioIdJson() {
        return (usuario != null) ? usuario.getId() : null;
    }

    @JsonProperty("usuarioNombre")
    public String getUsuarioNombreJson() {
        return (usuario != null) ? usuario.getNombre() : null;
    }

    @JsonProperty("usuarioApellido")
    public String getUsuarioApellidoJson() {
        return (usuario != null) ? usuario.getApellido() : null;
    }

    @JsonProperty("usuarioEmail")
    public String getUsuarioEmailJson() {
        return (usuario != null) ? usuario.getEmail() : null;
    }

}
