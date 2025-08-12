package com.bioesencia.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
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
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fechaOrden = LocalDateTime.now();

    @NotNull
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference("usuario-orden")
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    @JsonManagedReference("orden-item")
    private List<OrderItem> items;


}
