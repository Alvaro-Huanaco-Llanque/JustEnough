package com.justenough.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir credenciales (cookies, headers de autorización como Bearer token)
        config.setAllowCredentials(true);
        
        // Permitir orígenes del frontend (puertos típicos de React / Vite)
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        
        // Permitir todos los encabezados estándar
        config.setAllowedHeaders(List.of("*"));
        
        // Permitir los métodos HTTP principales
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}