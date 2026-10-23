package com.justenough.service;

import com.justenough.dto.VentasBatchRequestDto;
import com.justenough.model.Producto;
import com.justenough.model.RegistroDiario;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RegistroDiarioService {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void registrarVentasBatch(VentasBatchRequestDto request) {
        for (Map.Entry<Long, Double> entry : request.getVentas().entrySet()) {
            Long productoId = entry.getKey();
            Double cantidadVendida = entry.getValue();

            if (cantidadVendida == null || cantidadVendida <= 0) continue;

            Producto producto = productoRepository.findById(productoId)
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
            registroDiarioRepository.save(registro);
        }
    }
}
