package com.justenough.repository;

import com.justenough.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Método personalizado útil para Spring Security / Autenticación
    Optional<Usuario> findByEmail(String email);
}
