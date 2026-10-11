package com.justenough.controller;

import com.justenough.dto.UsuarioRegistroRequestDto;
import com.justenough.dto.UsuarioResponseDto;
import com.justenough.mapper.UsuarioMapper;
import com.justenough.model.Usuario;
import com.justenough.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private com.justenough.security.JwtUtil jwtUtil;

    // Listar todos los usuarios usando el DTO de respuesta (sin exponer contraseñas)
    @GetMapping
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioService.listarUsuarios().stream()
                .map(usuarioMapper::toDto)
                .toList();
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(usuarioMapper.toDto(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Registrar un nuevo usuario usando el DTO de request
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registrarUsuario(@Valid @RequestBody UsuarioRegistroRequestDto requestDto) {
        Usuario usuarioCreado = usuarioService.registrarUsuarioDto(requestDto, requestDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toDto(usuarioCreado));
    }

    // Desactivar un usuario (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        try {
            usuarioService.desactivarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para Iniciar Sesión y obtener el Token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody com.justenough.dto.LoginRequestDto loginRequest) {
        try {
            // Autenticamos al usuario
            Usuario usuario = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getPassword());
            
            // Generamos el token JWT con su email
            String token = jwtUtil.generarToken(usuario.getEmail());

            // Devolvemos el token en un JSON prolijo
            return ResponseEntity.ok(java.util.Collections.singletonMap("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
