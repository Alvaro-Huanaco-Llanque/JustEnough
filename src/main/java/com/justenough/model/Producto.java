package com.justenough.model;

import com.justenough.model.enums.UnidadMedida;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnidadMedida unidadMedida;

    @Column(nullable = false)
    private int vidaUtilDias;

    @Builder.Default
    @Column(nullable = false)
    private boolean priorizado = false;

    @Builder.Default
    @Column(nullable = false)
    private double stockMinimo = 0.0;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;
}
