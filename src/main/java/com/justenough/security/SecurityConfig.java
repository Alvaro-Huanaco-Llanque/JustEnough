package com.justenough.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitamos CSRF ya que usamos una API REST con JWT
            .csrf(csrf -> csrf.disable())
            // Habilitamos CORS para que permita la conexión con React
            .cors(cors -> {})
            // Configuramos la política de sesiones como Stateless (sin estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Definimos los permisos de las rutas
            .authorizeHttpRequests(auth -> auth
                // Permitimos acceso público al registro, login y listado de usuarios (podes ajustarlo)
                .requestMatchers("/api/usuarios/registro", "/api/usuarios/login", "/api/usuarios/**").permitAll()
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
