package com.justenough.dto;

import com.justenough.model.enums.Rubro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRegistroRequestDto {
    // Datos del usuario (dueño)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    // Datos del comercio
    @NotBlank(message = "El nombre del comercio es obligatorio")
    private String nombreComercio;

    @NotNull(message = "El rubro es obligatorio")
    private Rubro rubro;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;

    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    // Ignoramos espacios al principio/final para que no falle la validación por un copy-paste
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}
