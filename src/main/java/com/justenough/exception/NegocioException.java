package com.justenough.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

// Error de negocio con el código HTTP que corresponde devolver al cliente
@Getter
public class NegocioException extends RuntimeException {

    private final HttpStatus status;

    public NegocioException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static NegocioException noEncontrado(String message) {
        return new NegocioException(HttpStatus.NOT_FOUND, message);
    }

    public static NegocioException conflicto(String message) {
        return new NegocioException(HttpStatus.CONFLICT, message);
    }

    public static NegocioException noAutorizado(String message) {
        return new NegocioException(HttpStatus.UNAUTHORIZED, message);
    }
}
