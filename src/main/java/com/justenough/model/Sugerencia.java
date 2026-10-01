package com.justenough.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.justenough.model.enums.EstadoSugerencia;
import com.justenough.model.enums.NivelConfianza;

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
@Table(name = "sugerencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sugerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private double cantidadSugerida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelConfianza nivelConfianza;

    @Column(nullable = false)
    private double cantidadFinal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private EstadoSugerencia estado = EstadoSugerencia.PENDIENTE;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime fechaGeneracion = LocalDateTime.now();
}