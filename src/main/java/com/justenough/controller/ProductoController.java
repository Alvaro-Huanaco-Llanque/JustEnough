package com.justenough.controller;

import com.justenough.dto.ProductoRequestDto;
import com.justenough.model.Insumo;
import com.justenough.model.Producto;
import com.justenough.model.RecetaItem;
import com.justenough.repository.InsumoRepository;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RecetaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") 
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RecetaItemRepository recetaItemRepository;

    @GetMapping
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

@PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody ProductoRequestDto dto) {
        // 1. Crear y guardar el Producto mapeando todos los campos del DTO
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setVidaUtilDias(dto.getVidaUtilDias());
        producto.setPriorizado(dto.isPriorizado());
        producto.setStock(dto.getStock()); // <-- ¡Acá estaba el faltante clave!
        producto.setActivo(dto.isActivo());
        
        Producto productoGuardado = productoRepository.save(producto);

        // 2. Guardar la receta asociada si trae insumos
        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                // Lógica de receta...
            }
        }

        return ResponseEntity.ok(productoGuardado);
    }

@DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            // Si tienes elementos en receta asociados a este producto, los borramos primero para evitar conflicto de FK
            // (Si ya tienes un método deleteByProductoId en tu RecetaItemRepository, puedes descomentar la siguiente línea)
            // recetaItemRepository.deleteByProductoId(id);

            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}