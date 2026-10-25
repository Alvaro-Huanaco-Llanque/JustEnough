package com.justenough.service;

import com.justenough.dto.UsuarioRegistroRequestDto;
import com.justenough.exception.NegocioException;
import com.justenough.model.Comercio;
import com.justenough.model.Usuario;
import com.justenough.model.enums.Rol;
import com.justenough.repository.ComercioRepository;
import com.justenough.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComercioRepository comercioRepository;

    // encriptar contraseña
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Registra el comercio y su dueño en una sola transacción: si algo falla, no queda nada a medias
    @Transactional
    public Usuario registrarUsuarioDto(UsuarioRegistroRequestDto dto) {
        String email = normalizarEmail(dto.getEmail());
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw NegocioException.conflicto("Ya existe una cuenta registrada con ese email.");
        }

        Comercio comercio = comercioRepository.save(Comercio.builder()
                .nombre(dto.getNombreComercio().trim())
                .rubro(dto.getRubro())
                .direccion(dto.getDireccion().trim())
                .localidad(dto.getLocalidad().trim())
                .provincia(dto.getProvincia().trim())
                .telefono(dto.getTelefono().trim())
                .build());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setEmail(email);
        usuario.setActivo(true);
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(Rol.DUENIO);
        usuario.setComercio(comercio);
        usuario.setEmailVerificado(false);
        generarTokenVerificacion(usuario);

        return usuarioRepository.save(usuario);
    }

    // Marca el email como verificado si el token del link es válido y no venció
    @Transactional
    public Usuario verificarEmail(String token) {
        Usuario usuario = (token == null || token.isBlank()) ? null
                : usuarioRepository.findByTokenVerificacion(token).orElse(null);
        if (usuario == null) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "El link de verificación no es válido o ya fue usado.");
        }
        if (usuario.getTokenVerificacionExpira() == null || usuario.getTokenVerificacionExpira().isBefore(LocalDateTime.now())) {
            throw new NegocioException(HttpStatus.GONE, "El link de verificación venció. Pedí uno nuevo.");
        }
        usuario.setEmailVerificado(true);
        usuario.setTokenVerificacion(null);
        usuario.setTokenVerificacionExpira(null);
        return usuarioRepository.save(usuario);
    }

    // Genera un link nuevo para una cuenta que todavía no verificó su email.
    // Devuelve vacío si el email no existe o ya está verificado (el controller responde lo mismo en todos los casos).
    @Transactional
    public Optional<Usuario> renovarTokenVerificacion(String email) {
        return usuarioRepository.findByEmail(normalizarEmail(email))
                .filter(u -> !u.isEmailVerificado())
                .map(u -> {
                    generarTokenVerificacion(u);
                    return usuarioRepository.save(u);
                });
    }

    private void generarTokenVerificacion(Usuario usuario) {
        usuario.setTokenVerificacion(UUID.randomUUID().toString());
        usuario.setTokenVerificacionExpira(LocalDateTime.now().plusHours(24));
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Buscar usuario por email (clave para el login)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(normalizarEmail(email));
    }

    // Desactivar usuario (baja lógica en lugar de borrar el registro)
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> NegocioException.noEncontrado("Usuario no encontrado con ID: " + id));

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // Autenticar un usuario y devolver la entidad si las credenciales son correctas.
    // El mensaje es el mismo si falla el email o la contraseña, para no revelar qué cuentas existen.
    public Usuario autenticar(String email, String passwordPlana) {
        Usuario usuario = usuarioRepository.findByEmail(normalizarEmail(email))
                .filter(u -> passwordEncoder.matches(passwordPlana, u.getPasswordHash()))
                .orElseThrow(() -> NegocioException.noAutorizado("Email o contraseña incorrectos."));

        if (!usuario.isActivo()) {
            throw NegocioException.noAutorizado("Tu cuenta está desactivada.");
        }

        // 403 (y no 401) para que el frontend sepa que tiene que ofrecer reenviar el mail
        if (!usuario.isEmailVerificado()) {
            throw new NegocioException(HttpStatus.FORBIDDEN, "Tenés que verificar tu email antes de iniciar sesión. Revisá tu casilla de correo.");
        }

        return usuario;
    }

    public String encriptar(String passwordPlana) {
        return passwordEncoder.encode(passwordPlana);
    }

    public boolean coincidePassword(String passwordPlana, String hash) {
        return passwordEncoder.matches(passwordPlana, hash);
    }

    public static String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .roles(usuario.getRol().name())
                .disabled(!usuario.isActivo())
                .build();
    }

}
