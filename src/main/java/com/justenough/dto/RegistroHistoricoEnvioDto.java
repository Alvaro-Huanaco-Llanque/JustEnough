package com.justenough.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("cantidad_producida")
    private double cantidadProducida;

    @JsonProperty("cantidad_vendida")
    private double cantidadVendida;

    @JsonProperty("cantidad_desperdiciada")
    private double cantidadDesperdiciada;

    @JsonProperty("es_feriado_o_evento")
    private boolean esFeriadoOEvento;
}