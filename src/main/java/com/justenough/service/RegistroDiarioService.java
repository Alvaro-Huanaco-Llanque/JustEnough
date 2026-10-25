package com.justenough.service;

import com.justenough.dto.IngresosBatchRequestDto;
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

import java.time.LocalDate;
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

    public List<RegistroDiario> listarPorFecha(LocalDate fecha) {
        return registroDiarioRepository.findByProductoComercioIdAndFecha(sesionService.getComercioActual().getId(), fecha);
    }

    // Guarda cuánto se produjo o compró de cada producto en un día.
    // La cantidad es el total del día (no se suma): si se corrige un día ya cargado, el stock
    // se ajusta por la diferencia entre lo nuevo y lo que había.
    @Transactional
    public void registrarIngresosBatch(IngresosBatchRequestDto request) {
        Usuario usuario = sesionService.getUsuarioActual();
        Long comercioId = sesionService.getComercioActual().getId();

        for (Map.Entry<Long, Double> entry : request.getIngresos().entrySet()) {
            Long productoId = entry.getKey();
            double cantidadNueva = entry.getValue() == null ? 0.0 : entry.getValue();
            if (cantidadNueva < 0) {
                throw new IllegalArgumentException("La cantidad no puede ser negativa.");
            }

            Producto producto = productoRepository.findByIdAndComercioId(productoId, comercioId)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

            Optional<RegistroDiario> existente = registroDiarioRepository.findByProductoIdAndFecha(productoId, request.getFecha());
            double cantidadAnterior = existente.map(RegistroDiario::getCantidadProducida).orElse(0.0);
            // Nada que guardar: no había carga ese día y tampoco se cargó ahora
            if (existente.isEmpty() && cantidadNueva == 0) continue;

            double diferencia = cantidadNueva - cantidadAnterior;
            if (producto.getStock() + diferencia < 0) {
                throw new IllegalArgumentException("No se puede bajar el ingreso de " + producto.getNombre()
                        + " a " + cantidadNueva + ": parte de ese stock ya se vendió o descartó.");
            }
            producto.setStock(producto.getStock() + diferencia);
            productoRepository.save(producto);

            RegistroDiario registro = existente.orElse(RegistroDiario.builder()
                    .producto(producto)
                    .fecha(request.getFecha())
                    .cantidadProducida(0.0)
                    .cantidadVendida(0.0)
                    .cantidadDesperdiciada(0.0)
                    .build());
            registro.setCantidadProducida(cantidadNueva);
            registro.setModalidad(producto.getModalidad());
            registro.setCostoUnitario(producto.getCostoUnitario());
            registro.setCargadoPor(usuario);
            registroDiarioRepository.save(registro);
        }
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
