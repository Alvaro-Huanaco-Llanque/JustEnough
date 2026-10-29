package com.justenough.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrediccionRequestDto {
    @JsonProperty("producto_id")
    private Long productoId;

    @JsonProperty("nombre_producto")
    private String nombreProducto;

    @JsonProperty("dias_historicos_disponibles")
    private int diasHistoricosDisponibles;

    private List<RegistroHistoricoEnvioDto> historial;
}