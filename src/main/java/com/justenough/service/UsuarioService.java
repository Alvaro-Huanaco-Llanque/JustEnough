package com.justenough.service;

import com.justenough.model.Usuario;
import com.justenough.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // encriptar contraseña 
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Crear / Registrar un usuario nuevo con contraseña encriptada
    @Transactional
public Usuario registrarUsuarioDto(com.justenough.dto.UsuarioRegistroRequestDto dto, String passwordPlana) {
    if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
        throw new RuntimeException("Ya existe un usuario registrado con ese email.");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(dto.getNombre());
    usuario.setEmail(dto.getEmail());
    usuario.setActivo(true);
    usuario.setPasswordHash(passwordEncoder.encode(passwordPlana));
    
    return usuarioRepository.save(usuario);
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
        return usuarioRepository.findByEmail(email);
    }

    // Desactivar usuario (baja lógica en lugar de borrar el registro)
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
    
    // Autenticar un usuario y devolver la entidad si las credenciales son correctas
    public Usuario autenticar(String email, String passwordPlana) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas (email no encontrado)."));

        if (!usuario.isActivo()) {
            throw new RuntimeException("El usuario se encuentra desactivado.");
        }

        // Comparamos la contraseña ingresada con el hash almacenado
        if (!passwordEncoder.matches(passwordPlana, usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas (contraseña incorrecta).");
        }

        return usuario;
    }
}

