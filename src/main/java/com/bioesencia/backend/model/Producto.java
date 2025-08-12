package com.bioesencia.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @Lob
    @NotBlank
    private String descripcion;

    @NotNull
    private BigDecimal precio;

    @NotNull
    private Integer stock;

    private String imagenUrl;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;

    // Relaciones
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> items;
}
