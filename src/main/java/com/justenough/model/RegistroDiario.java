package com.justenough.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registros_diarios", uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "fecha"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private double cantidadProducida;

    @Column(nullable = false)
    private double cantidadVendida;

    private Double cantidadDesperdiciada; // Permite null si no se cargó

    @ManyToOne
    private Usuario cargadoPor;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime fechaCarga = LocalDateTime.now();
}