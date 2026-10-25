package com.justenough.dto;

import com.justenough.model.enums.Rol;
import com.justenough.model.enums.Rubro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponseDto {
    private Long id;
    private String nombre;
    private String email;
    private Rol rol;
    private boolean activo;
    private ComercioResumenDto comercio;

    @Data
    @Builder
    public static class ComercioResumenDto {
        private Long id;
        private String nombre;
        private Rubro rubro;
    }
}
