package com.bioesencia.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    @Column(name = "codigo_orden", nullable = false, unique = true)
    private String codigoOrden;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonBackReference
    private Usuario usuario;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @PrePersist
    protected void prePersist() {
        this.fechaOrden = LocalDateTime.now();
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
