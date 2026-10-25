package com.justenough.model;

import com.justenough.model.enums.ModalidadProducto;
import com.justenough.model.enums.UnidadMedida;
import jakarta.persistence.*;
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
    @Builder.Default
    private boolean priorizado = false;

    @Column(nullable = false)
    @Builder.Default
    private double stockMinimo = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private double stock = 0.0;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(nullable = false)
    @Builder.Default
    private double precioVenta = 0.0;

    // El default en la base hace que los productos que ya existían queden como PRODUCIDO
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) not null default 'PRODUCIDO'")
    @Builder.Default
    private ModalidadProducto modalidad = ModalidadProducto.PRODUCIDO;

    // Lo que le cuesta al comercio cada unidad: el precio de compra o el costo de producirla
    @Column(nullable = false)
    @Builder.Default
    private double costoUnitario = 0.0;
}
