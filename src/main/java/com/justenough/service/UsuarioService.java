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
    public Usuario registrarUsuario(Usuario usuario, String passwordPlana) {
        // Validar si ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario registrado con ese email.");
        }

        // Encriptar la contraseña antes de asignarla al hash
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
}
