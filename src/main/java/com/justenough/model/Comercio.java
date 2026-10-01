package com.justenough.model;

import java.time.LocalDate;

import com.justenough.model.enums.Rubro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/// El @ Indica que es una anotación de java (@Entity, @Id, etc.), Es lo que le dice a Spring "esta clase se guarda en MySQL". 
@Entity
@Table(name = "comercios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comercio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rubro rubro;

    @Column(nullable = false)
    private String localidad;

    @Column(nullable = false)
    private String provincia;

    @Builder.Default
    @Column(nullable = false)
    private LocalDate fechaAlta = LocalDate.now();

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;
}
