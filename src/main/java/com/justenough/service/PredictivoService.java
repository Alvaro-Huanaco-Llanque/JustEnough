package com.justenough.service;

import com.justenough.dto.PrediccionRequestDto;
import com.justenough.dto.RegistroHistoricoEnvioDto;
import com.justenough.dto.SugerenciaResponseDto;
import com.justenough.model.Producto;
import com.justenough.model.RegistroDiario;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RegistroDiarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictivoService {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RestTemplate restTemplate;

    // URL de tu microservicio de Python
    private final String PYTHON_URL = "http://localhost:8000/api/predecir";

    public SugerenciaResponseDto solicitarPrediccionParaProducto(Long productoId) {
        // 1. Buscar el producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // 2. Obtener el historial de registros diarios de ese producto
        List<RegistroDiario> registros = registroDiarioRepository.findAll()
                .stream()
                .filter(r -> r.getProducto().getId().equals(productoId))
                .collect(Collectors.toList());

        // 3. Mapear los registros a los DTOs que espera Python
        List<RegistroHistoricoEnvioDto> historialDto = registros.stream().map(r -> 
            RegistroHistoricoEnvioDto.builder()
                .fecha(r.getFecha().toString())
                .cantidadProducida(r.getCantidadProducida())
                .cantidadVendida(r.getCantidadVendida())
                .cantidadDesperdiciada(r.getCantidadDesperdiciada() != null ? r.getCantidadDesperdiciada() : 0.0)
                .esFeriadoOEvento(false) // Aquí puedes cruzar luego con tu tabla de EventoCalendario si la fecha coincide
                .build()
        ).collect(Collectors.toList());

        int diasDisponibles = historialDto.size();

        // 4. Armar el payload (request)
        PrediccionRequestDto requestDto = PrediccionRequestDto.builder()
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .diasHistoricosDisponibles(diasDisponibles)
                .historial(historialDto)
                .build();

        // 5. Hacer la petición POST al microservicio de Python y retornar la respuesta
        try {
            SugerenciaResponseDto respuesta = restTemplate.postForObject(PYTHON_URL, requestDto, SugerenciaResponseDto.class);
            return respuesta;
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio predictivo de Python: " + e.getMessage());
        }
    }
}