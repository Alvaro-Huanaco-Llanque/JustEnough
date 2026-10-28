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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PredictivoService {

    @Autowired
    private RegistroDiarioRepository registroDiarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EventoCalendarioService eventoCalendarioService;

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

        // Días con feriado nacional o evento propio del comercio dentro del historial
        Set<LocalDate> diasEspeciales = registros.isEmpty() || producto.getComercio() == null
                ? Set.of()
                : eventoCalendarioService.fechasConFeriadoOEvento(
                        producto.getComercio().getId(),
                        registros.stream().map(RegistroDiario::getFecha).min(LocalDate::compareTo).get(),
                        registros.stream().map(RegistroDiario::getFecha).max(LocalDate::compareTo).get());

        // 3. Mapear los registros a los DTOs que espera Python
        List<RegistroHistoricoEnvioDto> historialDto = registros.stream().map(r ->
            RegistroHistoricoEnvioDto.builder()
                .fecha(r.getFecha().toString())
                .cantidadProducida(r.getCantidadProducida())
                .cantidadVendida(r.getCantidadVendida())
                .cantidadDesperdiciada(r.getCantidadDesperdiciada() != null ? r.getCantidadDesperdiciada() : 0.0)
                .esFeriadoOEvento(diasEspeciales.contains(r.getFecha()))
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