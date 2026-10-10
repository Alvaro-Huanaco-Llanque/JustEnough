package com.justenough.controller;

import com.justenough.model.Sugerencia;
import com.justenough.repository.SugerenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sugerencias")
public class SugerenciaController {

    @Autowired
    private SugerenciaRepository sugerenciaRepository;

    @GetMapping
    public List<Sugerencia> listar() {
        return sugerenciaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sugerencia> buscarPorId(@PathVariable Long id) {
        return sugerenciaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sugerencia guardar(@RequestBody Sugerencia sugerencia) {
        return sugerenciaRepository.save(sugerencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!sugerenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        sugerenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
