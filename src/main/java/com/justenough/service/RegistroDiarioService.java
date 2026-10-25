package com.justenough.service;

import com.justenough.dto.VentasBatchRequestDto;
import com.justenough.exception.NegocioException;
import com.justenough.model.Producto;
import com.justenough.model.RegistroDiario;
import com.justenough.model.Usuario;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// Todas las operaciones trabajan solo sobre los registros de productos del comercio del usuario logueado
@Service
public class RegistroDiarioService {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SesionService sesionService;

    public List<RegistroDiario> listar() {
        return registroDiarioRepository.findByProductoComercioId(sesionService.getComercioActual().getId());
    }

    public Optional<RegistroDiario> buscarPorId(Long id) {
        return registroDiarioRepository.findByIdAndProductoComercioId(id, sesionService.getComercioActual().getId());
    }

    @Transactional
    public RegistroDiario guardar(RegistroDiario registro) {
        Usuario usuario = sesionService.getUsuarioActual();
        Long productoId = registro.getProducto() == null ? null : registro.getProducto().getId();
        Producto producto = productoId == null ? null
                : productoRepository.findByIdAndComercioId(productoId, sesionService.getComercioActual().getId()).orElse(null);
        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + productoId);
        }
        registro.setProducto(producto);
        registro.setCargadoPor(usuario);
        return registroDiarioRepository.save(registro);
    }

    @Transactional
    public void eliminar(Long id) {
        RegistroDiario registro = buscarPorId(id)
                .orElseThrow(() -> NegocioException.noEncontrado("Registro no encontrado con ID: " + id));
        registroDiarioRepository.delete(registro);
    }

    @Transactional
    public void registrarVentasBatch(VentasBatchRequestDto request) {
        Usuario usuario = sesionService.getUsuarioActual();
        Long comercioId = sesionService.getComercioActual().getId();

        for (Map.Entry<Long, Double> entry : request.getVentas().entrySet()) {
            Long productoId = entry.getKey();
            Double cantidadVendida = entry.getValue();

            if (cantidadVendida == null || cantidadVendida <= 0) continue;

            Producto producto = productoRepository.findByIdAndComercioId(productoId, comercioId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

            if (producto.getStock() < cantidadVendida) {
                throw new IllegalArgumentException("Stock insuficiente para " + producto.getNombre() + ". Stock actual: " + producto.getStock());
            }

            // Descontar stock del inventario
            producto.setStock(producto.getStock() - cantidadVendida);
            productoRepository.save(producto);

            // Buscar registro diario existente o crear uno nuevo
            RegistroDiario registro = registroDiarioRepository.findByProductoIdAndFecha(productoId, request.getFecha())
                    .orElse(RegistroDiario.builder()
                            .producto(producto)
                            .fecha(request.getFecha())
                            .cantidadProducida(0.0)
                            .cantidadVendida(0.0)
                            .cantidadDesperdiciada(0.0)
                            .build());

            // Sumar la nueva venta al registro del día
            registro.setCantidadVendida(registro.getCantidadVendida() + cantidadVendida);
            registro.setCargadoPor(usuario);
            registroDiarioRepository.save(registro);
        }
    }
}
