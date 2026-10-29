package com.justenough.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SugerenciaResponseDto {
    @JsonAlias({"producto_id", "productoId"})
    private Long productoId;

    @JsonAlias({"nombre_producto", "nombreProducto"})
    private String nombreProducto;

    @JsonAlias({"cantidad_sugerida", "cantidadSugerida"})
    private double cantidadSugerida;

    @JsonAlias({"nivel_confianza", "nivelConfianza"})
    private String nivelConfianza;

    @JsonAlias({"mensaje_explicativo", "mensajeExplicativo"})
    private String mensajeExplicativo;
}