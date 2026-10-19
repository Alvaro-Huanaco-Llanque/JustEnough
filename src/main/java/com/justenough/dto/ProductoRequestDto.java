package com.justenough.dto;

import com.justenough.model.enums.UnidadMedida;
import lombok.Data;
import java.util.List;

@Data
public class ProductoRequestDto {
    private String nombre;
    private UnidadMedida unidadMedida;
    private int vidaUtilDias;
    private boolean priorizado;
    private double stock;     // <-- ¡Agregado aquí!
    private boolean activo;   // <-- ¡Y agregado aquí!
    private List<RecetaDto> insumos;

    @Data
    public static class RecetaDto {
        private String ingrediente;
        private double cantidad;
    }
}
