package com.justenough.dto;

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
    private Long productoId;
    private String nombreProducto;
    private int diasHistoricosDisponibles;
    private List<RegistroHistoricoEnvioDto> historial;
}