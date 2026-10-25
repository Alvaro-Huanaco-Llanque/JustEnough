package com.justenough.service;

import com.justenough.exception.NegocioException;
import com.justenough.model.Comercio;
import com.justenough.model.Usuario;
import com.justenough.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// Da acceso al usuario logueado (el del token JWT) y a su comercio,
// para que cada cuenta vea y modifique solamente sus propios datos.
@Service
public class SesionService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario getUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw NegocioException.noAutorizado("Tenés que iniciar sesión.");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> NegocioException.noAutorizado("La sesión no es válida. Volvé a iniciar sesión."));
    }

    public Comercio getComercioActual() {
        Comercio comercio = getUsuarioActual().getComercio();
        if (comercio == null) {
            throw new NegocioException(HttpStatus.FORBIDDEN, "Tu usuario no tiene un comercio asociado.");
        }
        return comercio;
    }
}
