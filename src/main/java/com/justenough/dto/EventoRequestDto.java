package com.justenough.dto;

import com.justenough.model.enums.ImpactoEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

// Alta de un evento propio del comercio (feria, partido, corte de calle...)
@Data
public class EventoRequestDto {
    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(max = 120, message = "El nombre puede tener hasta 120 caracteres")
    private String nombre;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String categoria;

    @NotNull(message = "Indicá el impacto esperado")
    private ImpactoEvento impacto;

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.trim();
    }
}
