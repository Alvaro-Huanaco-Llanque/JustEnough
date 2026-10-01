package com.justenough.model;

import java.time.LocalDateTime;

import com.justenough.model.enums.TipoAlerta;

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
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;

    @ManyToOne
    private Producto producto;   // puede ser null (ej: alerta de inactividad es de todo el comercio)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipo;

    @Column(nullable = false)
    private String mensaje;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Builder.Default
    @Column(nullable = false)
    private boolean leida = false;
}