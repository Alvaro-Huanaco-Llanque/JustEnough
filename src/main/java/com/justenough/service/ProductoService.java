package com.justenough.service;

import com.justenough.dto.ProductoRequestDto;
import com.justenough.model.Insumo;
import com.justenough.model.Producto;
import com.justenough.model.RecetaItem;
import com.justenough.repository.InsumoRepository;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RecetaItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RecetaItemRepository recetaItemRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Transactional
    public Producto guardar(ProductoRequestDto dto) {
        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombre(recetaDto.getIngrediente())
                        .orElseThrow(() -> new IllegalArgumentException("El insumo '" + recetaDto.getIngrediente() + "' no existe."));
                if (insumo.getStockActual() < recetaDto.getCantidad()) {
                    throw new IllegalArgumentException("No hay suficiente stock de " + insumo.getNombre());
                }
            }
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setPriorizado(dto.isPriorizado());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.isActivo());
        
        Producto productoGuardado = productoRepository.save(producto);

        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombre(recetaDto.getIngrediente()).get();
                insumo.setStockActual(insumo.getStockActual() - recetaDto.getCantidad());
                insumoRepository.save(insumo);

                RecetaItem recetaItem = new RecetaItem();
                recetaItem.setProducto(productoGuardado);
                recetaItem.setInsumo(insumo);
                recetaItem.setCantidad(recetaDto.getCantidad());
                recetaItemRepository.save(recetaItem);
            }
        }
        return productoGuardado;
    }

    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado");
        }
        // Eliminamos la receta primero para evitar errores de llave foránea
        recetaItemRepository.deleteByProductoId(id);
        productoRepository.deleteById(id);
    }

    @Transactional
    public Producto actualizarStock(Long id, double nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarPrecio(Long id, double nuevoPrecio) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
        producto.setPrecioVenta(nuevoPrecio);
        return productoRepository.save(producto);
    }
}