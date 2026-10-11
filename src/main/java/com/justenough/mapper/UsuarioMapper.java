package com.justenough.mapper;

import com.justenough.dto.UsuarioResponseDto;
import com.justenough.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // Convierte de Entidad a Response DTO (para enviar al cliente sin exponer datos sensibles)
    public UsuarioResponseDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .activo(usuario.isActivo())
                .build();
    }

    // Convierte del DTO de registro a Entidad base
    public Usuario toEntity(com.justenough.dto.UsuarioRegistroRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setActivo(true);
        return usuario;
    }
}