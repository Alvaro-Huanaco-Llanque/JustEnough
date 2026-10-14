package com.justenough.dto;

import lombok.Data;

@Data
public class SugerenciaResponseDto {
    private Long productoId;
    private String nombreProducto;
    private double cantidadSugerida;
    private String nivelConfianza;
    private String mensajeExplicativo;
}