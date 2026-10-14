package com.justenough.controller;

import com.justenough.dto.SugerenciaResponseDto;
import com.justenough.service.PredictivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predicciones")
public class PrediccionController {

    @Autowired
    private PredictivoService predictivoService;

    @PostMapping("/producto/{productoId}")
    public ResponseEntity<SugerenciaResponseDto> generarPrediccion(@PathVariable Long productoId) {
        SugerenciaResponseDto resultado = predictivoService.solicitarPrediccionParaProducto(productoId);
        return ResponseEntity.ok(resultado);
    }
}