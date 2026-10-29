package com.justenough.dto;

import com.justenough.model.enums.ImpactoEvento;
import com.justenough.model.enums.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Un día marcado en el calendario: feriado, efeméride o evento propio del comercio
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoCalendarioDto {
    private Long id;            // null en las efemérides (no se guardan en la base)
    private LocalDate fecha;
    private TipoEvento tipo;    // FERIADO, FECHA_ESPECIAL (efeméride) o EVENTO_LOCAL (propio)
    private String titulo;
    private String categoria;
    private ImpactoEvento impacto;
    private boolean propio;     // true = lo cargó el comercio y lo puede borrar
}
