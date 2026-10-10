package com.justenough.controller;

import com.justenough.model.RegistroDiario;
import com.justenough.repository.RegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-diarios")
public class RegistroDiarioController {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @GetMapping
    public List<RegistroDiario> listar() {
        return registroDiarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroDiario> buscarPorId(@PathVariable Long id) {
        return registroDiarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RegistroDiario guardar(@RequestBody RegistroDiario registro) {
        return registroDiarioRepository.save(registro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!registroDiarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        registroDiarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}