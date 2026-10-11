package com.justenough.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String nombre;
    private String email;
    private boolean activo;
}
