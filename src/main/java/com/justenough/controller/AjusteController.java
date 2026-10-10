package com.justenough.controller;

import com.justenough.model.Ajuste;
import com.justenough.repository.AjusteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajustes")
public class AjusteController {

    @Autowired
    private AjusteRepository ajusteRepository;

    @GetMapping
    public List<Ajuste> listar() {
        return ajusteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ajuste> buscarPorId(@PathVariable Long id) {
        return ajusteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ajuste guardar(@RequestBody Ajuste ajuste) {
        return ajusteRepository.save(ajuste);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!ajusteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ajusteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
