package com.justenough.controller;

import com.justenough.model.Comercio;
import com.justenough.service.ComercioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    @Autowired
    private ComercioService comercioService;

    // Listar todos los comercios (GET /api/comercios)
    @GetMapping
    public List<Comercio> listarComercios() {
        return comercioService.listarComercios();
    }

    // Buscar comercio por ID (GET /api/comercios/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Comercio> buscarPorId(@PathVariable Long id) {
        return comercioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo comercio (POST /api/comercios)
    @PostMapping
    public Comercio crearComercio(@RequestBody Comercio comercio) {
        return comercioService.crearComercio(comercio);
    }

    // Actualizar un comercio (PUT /api/comercios/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Comercio> actualizarComercio(@PathVariable Long id, @RequestBody Comercio comercio) {
        try {
            Comercio actualizado = comercioService.actualizarComercio(id, comercio);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Desactivar un comercio (DELETE /api/comercios/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarComercio(@PathVariable Long id) {
        try {
            comercioService.desactivarComercio(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}