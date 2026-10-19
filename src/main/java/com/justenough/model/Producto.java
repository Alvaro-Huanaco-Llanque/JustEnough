package com.justenough.model;

import com.justenough.model.enums.UnidadMedida;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(nullable = false)
    private boolean priorizado = false;

    @Column(nullable = false)
    private double stockMinimo = 0.0;

    @Column(nullable = false)
    private double stock = 0.0; // <-- Asegúrate de que esté declarado exactamente así

    @Column(nullable = false)
    private boolean activo = true;
}
