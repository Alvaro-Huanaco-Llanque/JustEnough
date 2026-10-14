package com.justenough.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroHistoricoEnvioDto {
    private String fecha;
    private double cantidadProducida;
    private double cantidadVendida;
    private Double cantidadDesperdiciada;
    private boolean esFeriadoOEvento;
}