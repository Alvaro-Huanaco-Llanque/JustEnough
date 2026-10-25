package com.justenough.controller;

import com.justenough.dto.IngresosBatchRequestDto;
import com.justenough.dto.VentasBatchRequestDto;
import com.justenough.model.RegistroDiario;
import com.justenough.service.RegistroDiarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registros-diarios")
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioService registroDiarioService;

    // Sin fecha devuelve todos los registros del comercio; con ?fecha=AAAA-MM-DD solo los de ese día
    @GetMapping
    public List<RegistroDiario> listar(@RequestParam(required = false) LocalDate fecha) {
        return fecha == null ? registroDiarioService.listar() : registroDiarioService.listarPorFecha(fecha);
    }

    // Carga diaria de lo producido / comprado (pantalla Producido / Compras)
    @PostMapping("/ingresos-batch")
    public Map<String, String> registrarIngresos(@Valid @RequestBody IngresosBatchRequestDto request) {
        registroDiarioService.registrarIngresosBatch(request);
        return Map.of("message", "Ingresos guardados con éxito");
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
