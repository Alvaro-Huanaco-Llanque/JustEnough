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
        // 1. Validar stock de TODOS los insumos antes de guardar nada
        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombre(recetaDto.getIngrediente())
                        .orElseThrow(() -> new IllegalArgumentException("El insumo '" + recetaDto.getIngrediente() + "' no existe en la base de datos."));

                if (insumo.getStockActual() < recetaDto.getCantidad()) {
                    throw new IllegalArgumentException("No hay suficiente stock de " + insumo.getNombre() + ". Stock actual: " + insumo.getStockActual());
                }
            }
        }

        // 2. Crear y guardar el Producto
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setVidaUtilDias(dto.getVidaUtilDias());
        producto.setPriorizado(dto.isPriorizado());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.isActivo());
        
        Producto productoGuardado = productoRepository.save(producto);

        // 3. Descontar el stock de los insumos y guardar la receta
        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombre(recetaDto.getIngrediente()).get();
                
                // Restamos el stock utilizado
                insumo.setStockActual(insumo.getStockActual() - recetaDto.getCantidad());
                insumoRepository.save(insumo);

                // Guardamos el registro de la receta
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
}