package com.justenough.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class VentasBatchRequestDto {
    private LocalDate fecha;
    private Map<Long, Double> ventas; // Mapea el ID del producto con la cantidad vendida
}