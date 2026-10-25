package com.justenough.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // Ignoramos espacios al principio/final para que no falle la validación por un copy-paste
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }
}
