package com.bioesencia.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fechaHora;

    @NotNull
    private Integer duracion; // en minutos

    @NotBlank
    private String servicio; // ejemplo: "terapia Reiki"

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @Lob
    private String notas;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-cita")
    private Usuario usuario;

    @JsonProperty("usuarioNombre")
    public String getUsuarioNombreJson() {
        return (usuario != null) ? usuario.getNombre() : null;
    }

    @JsonProperty("usuarioCorreo")
    public String getUsuarioCorreoJson() {
        return (usuario != null) ? usuario.getEmail() : null;
    }
}
