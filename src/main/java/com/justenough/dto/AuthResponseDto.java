package com.justenough.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Lo que recibe el frontend al iniciar sesión o crear una cuenta
@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private UsuarioResponseDto usuario;
}
