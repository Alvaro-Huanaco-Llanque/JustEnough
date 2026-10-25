package com.justenough.controller;

import com.justenough.dto.VentasBatchRequestDto;
import com.justenough.model.RegistroDiario;
import com.justenough.service.RegistroDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-diarios")
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioService registroDiarioService;

    @GetMapping
    public List<RegistroDiario> listar() {
        return registroDiarioService.listar();
    }

    @PostMapping("/ventas-batch")
    public ResponseEntity<?> registrarVentas(@RequestBody VentasBatchRequestDto request) {
        try {
            registroDiarioService.registrarVentasBatch(request);
            return ResponseEntity.ok("{\"message\": \"Ventas registradas con éxito\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDiario> buscarPorId(@PathVariable Long id) {
        return registroDiarioService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RegistroDiario guardar(@RequestBody RegistroDiario registro) {
        return registroDiarioService.guardar(registro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        registroDiarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
