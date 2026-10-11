package com.justenough.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens (lo ideal es que luego la leas del .env)
    private final String SECRET_STRING = "esta_es_una_clave_secreta_muy_larga_y_segura_para_jwt_justenough_2026";
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());

    // Tiempo de expiración del token (24 horas en milisegundos)
    private final long EXPIRATION_TIME = 86400000;

    // Generar el token JWT usando el email del usuario
    public String generarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // Extraer el email (subject) del token
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Extraer la fecha de expiración del token
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    // Método genérico para extraer claims
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosLosClaims(token);
        return claimsResolver.apply(claims);
    }

    // Leer y verificar la firma del token
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Comprobar si el token ha expirado
    private Boolean estaExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    // Validar si el token es correcto y pertenece al usuario
    public Boolean validarToken(String token, String email) {
        final String emailExtraido = extraerEmail(token);
        return (emailExtraido.equals(email) && !estaExpirado(token));
    }
}