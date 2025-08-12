package com.bioesencia.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String telefono;

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    private boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-cita")
    private List<Cita> citas;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-orden")
    private List<Orden> ordenes;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference("usuario-inscripcion")
    private List<InscripcionTaller> inscripciones;

}