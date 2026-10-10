package com.justenough.controller;

import com.justenough.model.RecetaItem;
import com.justenough.repository.RecetaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recetas")
public class RecetaItemController {

    @Autowired
    private RecetaItemRepository recetaItemRepository;

    @GetMapping
    public List<RecetaItem> listar() {
        return recetaItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaItem> buscarPorId(@PathVariable Long id) {
        return recetaItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public RecetaItem guardar(@RequestBody RecetaItem recetaItem) {
        return recetaItemRepository.save(recetaItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!recetaItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recetaItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}