package com.justenough.service;

import com.justenough.dto.ProductoRequestDto;
import com.justenough.model.Comercio;
import com.justenough.model.Insumo;
import com.justenough.model.Producto;
import com.justenough.model.RecetaItem;
import com.justenough.repository.InsumoRepository;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RecetaItemRepository;
import com.justenough.repository.RegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Todas las operaciones trabajan solo sobre los productos del comercio del usuario logueado
@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RecetaItemRepository recetaItemRepository;

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private SesionService sesionService;

    public List<Producto> listar() {
        return productoRepository.findByComercioId(sesionService.getComercioActual().getId());
    }

    @Transactional
    public Producto guardar(ProductoRequestDto dto) {
        Comercio comercio = sesionService.getComercioActual();

        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombreAndComercioId(recetaDto.getIngrediente(), comercio.getId())
                        .orElseThrow(() -> new IllegalArgumentException("El insumo '" + recetaDto.getIngrediente() + "' no existe."));
                if (insumo.getStockActual() < recetaDto.getCantidad()) {
                    throw new IllegalArgumentException("No hay suficiente stock de " + insumo.getNombre());
                }
            }
        }

        Producto producto = new Producto();
        producto.setComercio(comercio); // <-- ESTA ES LA LÍNEA CLAVE QUE FALTABA
        producto.setNombre(dto.getNombre());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setPriorizado(dto.isPriorizado());
        producto.setStock(dto.isControlaStock() ? dto.getStock() : 0.0);
        producto.setActivo(dto.isActivo());
        producto.setControlaStock(dto.isControlaStock());
        if (dto.getPrecioVenta() > 0) {
            producto.setPrecioVenta(dto.getPrecioVenta());
        }

        Producto productoGuardado = productoRepository.save(producto);

        if (dto.getInsumos() != null && !dto.getInsumos().isEmpty()) {
            for (ProductoRequestDto.RecetaDto recetaDto : dto.getInsumos()) {
                Insumo insumo = insumoRepository.findByNombreAndComercioId(recetaDto.getIngrediente(), comercio.getId()).get();
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

    // Edita los datos del catálogo. El stock no se toca acá: se ajusta desde Inventario.
    @Transactional
    public Producto actualizar(Long id, ProductoRequestDto dto) {
        Producto producto = buscarPropio(id);
        copiarDatosDeCatalogo(dto, producto);
        return productoRepository.save(producto);
    }

    // Si el producto ya tiene ventas, producción o desechos cargados no se puede borrar
    // (se perdería el historial que usa la predicción): en ese caso se desactiva.
    // Devuelve true si se borró y false si solo se desactivó.
    @Transactional
    public boolean eliminar(Long id) {
        Producto producto = buscarPropio(id);
        if (registroDiarioRepository.existsByProductoId(producto.getId())) {
            producto.setActivo(false);
            productoRepository.save(producto);
            return false;
        }
        // Eliminamos la receta primero para evitar errores de llave foránea
        recetaItemRepository.deleteByProductoId(producto.getId());
        productoRepository.delete(producto);
        return true;
    }

    @Transactional
    public Producto actualizarStock(Long id, double nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        Producto producto = buscarPropio(id);
        producto.setStock(nuevoStock);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarPrecio(Long id, double nuevoPrecio) {
        Producto producto = buscarPropio(id);
        producto.setPrecioVenta(nuevoPrecio);
        return productoRepository.save(producto);
    }

    private void copiarDatosDeCatalogo(ProductoRequestDto dto, Producto producto) {
        producto.setNombre(dto.getNombre().trim());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setModalidad(dto.getModalidad());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setCostoUnitario(dto.getCostoUnitario());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setPriorizado(dto.isPriorizado());
        producto.setActivo(dto.isActivo());
    }

    // Busca el producto solo dentro del comercio del usuario: los de otros comercios "no existen"
    private Producto buscarPropio(Long id) {
        return productoRepository.findByIdAndComercioId(id, sesionService.getComercioActual().getId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }
}
