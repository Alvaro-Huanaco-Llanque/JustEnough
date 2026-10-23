package com.justenough.dto;

import com.justenough.model.enums.UnidadMedida;
import lombok.Data;
import java.util.List;

@Data
public class ProductoRequestDto {
    private String nombre;
    private UnidadMedida unidadMedida;
    private boolean priorizado;
    private double stock;
    private boolean activo;
    private List<RecetaDto> insumos;

    @Data
    public static class RecetaDto {
        private String ingrediente;
        private double cantidad;
    }
}
