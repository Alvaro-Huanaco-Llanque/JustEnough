package com.justenough.controller;

import com.justenough.dto.ProductoRequestDto;
import com.justenough.model.Producto;
import com.justenough.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Productos del catálogo del comercio logueado. Los errores de validación y de negocio
// los responde el GlobalExceptionHandler con {"message": ...}.
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    // Alta de un producto en el catálogo
    @PostMapping
    public Producto guardar(@Valid @RequestBody ProductoRequestDto dto) {
        return productoService.guardar(dto);
    }

    // Edición de un producto del catálogo (nombre, modalidad, precios, etc.)
    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequestDto dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        boolean borrado = productoService.eliminar(id);
        String mensaje = borrado
                ? "Producto eliminado."
                : "El producto tiene datos cargados (ventas, producción o desechos), así que se desactivó en lugar de borrarse.";
        return ResponseEntity.ok(Map.of("borrado", borrado, "message", mensaje));
    }

    // Ajuste manual del conteo desde Inventario
    @PutMapping("/{id}/stock")
    public Producto actualizarStock(@PathVariable Long id, @RequestParam double nuevoStock) {
        return productoService.actualizarStock(id, nuevoStock);
    }

    @PutMapping("/{id}/precio")
    public Producto actualizarPrecio(@PathVariable Long id, @RequestParam double nuevoPrecio) {
        return productoService.actualizarPrecio(id, nuevoPrecio);
    }
}
