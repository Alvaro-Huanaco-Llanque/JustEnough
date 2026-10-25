package com.justenough.mapper;

import com.justenough.dto.UsuarioResponseDto;
import com.justenough.model.Comercio;
import com.justenough.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // Convierte de Entidad a Response DTO (para enviar al cliente sin exponer datos sensibles)
    public UsuarioResponseDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        Comercio comercio = usuario.getComercio();
        UsuarioResponseDto.ComercioResumenDto comercioDto = comercio == null ? null
                : UsuarioResponseDto.ComercioResumenDto.builder()
                        .id(comercio.getId())
                        .nombre(comercio.getNombre())
                        .rubro(comercio.getRubro())
                        .build();

        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .comercio(comercioDto)
                .build();
    }
}
