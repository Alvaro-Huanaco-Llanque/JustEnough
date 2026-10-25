package com.justenough.controller;

import com.justenough.model.Insumo;
import com.justenough.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    // Listar todos los insumos (GET /api/insumos)
    @GetMapping
    public List<Insumo> listarInsumos() {
        return insumoService.listarInsumos();
    }

    // Buscar insumo por ID (GET /api/insumos/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Insumo> buscarPorId(@PathVariable Long id) {
        return insumoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear o guardar un insumo (POST /api/insumos)
    @PostMapping
    public Insumo guardarInsumo(@RequestBody Insumo insumo) {
        return insumoService.guardarInsumo(insumo);
    }

    // Actualizar el stock de un insumo (PUT /api/insumos/{id}/stock?nuevoStock=50)
    @PutMapping("/{id}/stock")
    public ResponseEntity<Insumo> actualizarStock(@PathVariable Long id, @RequestParam double nuevoStock) {
        return ResponseEntity.ok(insumoService.actualizarStock(id, nuevoStock));
    }

    // Eliminar un insumo (DELETE /api/insumos/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Long id) {
        insumoService.eliminarInsumo(id);
        return ResponseEntity.noContent().build();
    }
}
