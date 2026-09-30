package com.justenough.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ingredientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Ingrediente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private Double stockActual;

    @Column(nullable = false)
    private Double puntoDePedido; // Nivel crítico de stock para lanzar alertas

    @Column(nullable = false)
    private String unidadDeMedida; // (KG, LITROS, UNIDADES)
}
