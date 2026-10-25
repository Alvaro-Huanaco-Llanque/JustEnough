package com.justenough.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

// Lo que se produjo o compró en un día: ID del producto -> cantidad total que ingresó ese día
@Data
public class IngresosBatchRequestDto {
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "No hay cantidades para guardar")
    private Map<Long, Double> ingresos;
}
