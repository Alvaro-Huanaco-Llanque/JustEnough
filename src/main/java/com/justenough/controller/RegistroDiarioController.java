package com.justenough.controller;

import com.justenough.dto.VentasBatchRequestDto;
import com.justenough.model.RegistroDiario;
import com.justenough.repository.RegistroDiarioRepository;
import com.justenough.service.RegistroDiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-diarios")
@CrossOrigin(origins = "*") 
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private RegistroDiarioService registroDiarioService;

    @GetMapping
    public List<RegistroDiario> listar() {
        return registroDiarioRepository.findAll();
    }

    @PostMapping("/ventas-batch")
    public ResponseEntity<?> registrarVentas(@RequestBody VentasBatchRequestDto request) {
        try {
            registroDiarioService.registrarVentasBatch(request);
            return ResponseEntity.ok("{\"message\": \"Ventas registradas con éxito\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"message\": \"Error interno del servidor\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDiario> buscarPorId(@PathVariable Long id) {
        return registroDiarioRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RegistroDiario guardar(@RequestBody RegistroDiario registro) {
        return registroDiarioRepository.save(registro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!registroDiarioRepository.existsById(id)) return ResponseEntity.notFound().build();
        registroDiarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}