package com.justenough.controller;

import com.justenough.dto.AuthResponseDto;
import com.justenough.dto.LoginRequestDto;
import com.justenough.dto.UsuarioRegistroRequestDto;
import com.justenough.dto.UsuarioResponseDto;
import com.justenough.mapper.UsuarioMapper;
import com.justenough.model.Usuario;
import com.justenough.security.JwtUtil;
import com.justenough.service.EmailService;
import com.justenough.service.SesionService;
import com.justenough.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private SesionService sesionService;
    @Autowired
    private UsuarioMapper usuarioMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;

    // Listar todos los usuarios usando el DTO de respuesta (sin exponer contraseñas)
    @GetMapping
    public List<UsuarioResponseDto> listarUsuarios() {
        return usuarioService.listarUsuarios().stream()
                .map(usuarioMapper::toDto)
                .toList();
    }

    // Datos del usuario logueado y su comercio (GET /api/usuarios/perfil)
    @GetMapping("/perfil")
    public UsuarioResponseDto obtenerPerfil() {
        return usuarioMapper.toDto(sesionService.getUsuarioActual());
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(usuarioMapper.toDto(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear cuenta: registra comercio + dueño y manda el mail de verificación.
    // No devuelve token: la cuenta recién se puede usar después de verificar el email.
    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@Valid @RequestBody UsuarioRegistroRequestDto requestDto) {
        Usuario usuarioCreado = usuarioService.registrarUsuarioDto(requestDto);
        enviarMailVerificacion(usuarioCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Cuenta creada. Te enviamos un mail para verificarla.",
                "email", usuarioCreado.getEmail()));
    }

    // El frontend llama a este endpoint con el token del link del mail. Si es válido, deja la sesión iniciada.
    @PostMapping("/verificar")
    public AuthResponseDto verificarEmail(@RequestBody Map<String, String> body) {
        Usuario usuario = usuarioService.verificarEmail(body.get("token"));
        return construirRespuesta(usuario);
    }

    // Reenvía el mail de verificación. Responde siempre lo mismo para no revelar qué emails tienen cuenta.
    @PostMapping("/reenviar-verificacion")
    public Map<String, String> reenviarVerificacion(@RequestBody Map<String, String> body) {
        usuarioService.renovarTokenVerificacion(body.get("email")).ifPresent(this::enviarMailVerificacion);
        return Map.of("message", "Si la cuenta existe y todavía no está verificada, te enviamos un nuevo mail.");
    }

    // Desactivar un usuario (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para Iniciar Sesión y obtener el Token JWT.
    // Si las credenciales son incorrectas, el GlobalExceptionHandler responde 401 con {"message": ...}
    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto loginRequest) {
        Usuario usuario = usuarioService.autenticar(loginRequest.getEmail(), loginRequest.getPassword());
        return construirRespuesta(usuario);
    }

    // Si el mail falla (ej: credenciales de Gmail mal configuradas) la cuenta igual queda creada
    // y se puede pedir el reenvío; dejamos el error en la consola para poder diagnosticarlo.
    private void enviarMailVerificacion(Usuario usuario) {
        try {
            emailService.enviarVerificacion(usuario);
        } catch (Exception e) {
            System.out.println("[UsuarioController] No se pudo enviar el mail de verificación a " + usuario.getEmail() + ": " + e.getMessage());
        }
    }

    private AuthResponseDto construirRespuesta(Usuario usuario) {
        String token = jwtUtil.generarToken(usuario.getEmail());
        return new AuthResponseDto(token, usuarioMapper.toDto(usuario));
    }
}
