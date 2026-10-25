package com.justenough.dto;

import com.justenough.model.enums.ModalidadProducto;
import com.justenough.model.enums.UnidadMedida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.util.List;

// Datos de un producto del catálogo (alta y edición)
@Data
public class ProductoRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    @NotNull(message = "Indicá si el producto es producido o comprado")
    private ModalidadProducto modalidad;

    @PositiveOrZero(message = "El precio de venta no puede ser negativo")
    private double precioVenta;

    @PositiveOrZero(message = "El costo no puede ser negativo")
    private double costoUnitario;

    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private double stockMinimo;

    private boolean priorizado;
    private boolean activo = true;

    // Stock inicial: solo se usa al crear el producto
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private double stock;

    // Receta (insumos): por ahora no se usa desde el catálogo, queda para más adelante
    private List<RecetaDto> insumos;

    @Data
    public static class RecetaDto {
        private String ingrediente;
        private double cantidad;
    }
}
