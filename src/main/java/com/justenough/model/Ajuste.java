package com.justenough.model;

import java.time.LocalDateTime;

import com.justenough.model.enums.MotivoAjuste;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ajustes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ajuste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne   // cada sugerencia tiene como máximo UN ajuste
    private Sugerencia sugerencia;

    @Column(nullable = false)
    private double cantidadOriginal;

    @Column(nullable = false)
    private double cantidadAjustada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoAjuste motivo;

    private String comentario;

    @ManyToOne
    private Usuario usuario;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}