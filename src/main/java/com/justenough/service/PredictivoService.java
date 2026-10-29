package com.justenough.service;

import com.justenough.dto.PrediccionRequestDto;
import com.justenough.dto.RegistroHistoricoEnvioDto;
import com.justenough.dto.SugerenciaResponseDto;
import com.justenough.model.Comercio;
import com.justenough.model.Producto;
import com.justenough.model.RegistroDiario;
import com.justenough.model.Sugerencia;
import com.justenough.model.enums.EstadoSugerencia;
import com.justenough.model.enums.NivelConfianza;
import com.justenough.repository.ProductoRepository;
import com.justenough.repository.RegistroDiarioRepository;
import com.justenough.repository.SugerenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private SugerenciaRepository sugerenciaRepository;

    @Autowired
    private SesionService sesionService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EventoCalendarioService eventoCalendarioService;

    // URL de tu microservicio de Python
    private final String PYTHON_URL = "http://localhost:8000/api/predecir";

    public SugerenciaResponseDto solicitarPrediccionParaProducto(Long productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        List<RegistroDiario> registros = registroDiarioRepository.findAll()
                .stream()
                .filter(r -> r.getProducto() != null && r.getProducto().getId().equals(productoId))
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

        PrediccionRequestDto requestDto = PrediccionRequestDto.builder()
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .diasHistoricosDisponibles(historialDto.size())
                .historial(historialDto)
                .build();

        try {
            SugerenciaResponseDto respuesta = restTemplate.postForObject(PYTHON_URL, requestDto, SugerenciaResponseDto.class);
            if (respuesta != null && respuesta.getCantidadSugerida() > 0) {
                return respuesta;
            }
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo conectar con Python (" + e.getMessage() + "). Usando cálculo local.");
        }

        // Respaldo local con la misma fórmula de Python por si el servidor FastAPI no está corriendo
        double promedio = registros.stream().mapToDouble(RegistroDiario::getCantidadVendida).average().orElse(10.0);
        int dias = historialDto.size();
        String confianza = dias < 30 ? "BAJA" : (dias < 60 ? "MEDIA" : "ALTA");

        return SugerenciaResponseDto.builder()
                .productoId(producto.getId())
                .nombreProducto(producto.getNombre())
                .cantidadSugerida(Math.round(promedio))
                .nivelConfianza(confianza)
                .mensajeExplicativo("Calculado con historial de " + dias + " días.")
                .build();
    }

    @Transactional
    public List<Sugerencia> obtenerOGenerarSemana(LocalDate lunesSemana, boolean forzarRegeneracion) {
        Comercio comercio = sesionService.getComercioActual();
        LocalDate domingoSemana = lunesSemana.plusDays(6);

        List<Producto> productos = productoRepository.findByComercioId(comercio.getId())
                .stream()
                .filter(p -> p.isActivo() && p.isControlaStock())
                .collect(Collectors.toList());

        if (!forzarRegeneracion) {
            List<Sugerencia> existentes = sugerenciaRepository.findByProductoComercioIdAndFechaBetween(
                    comercio.getId(), lunesSemana, domingoSemana);
            // Si ya están generadas las sugerencias para todos los productos de la semana, las devolvemos directo
            if (!existentes.isEmpty() && existentes.size() >= productos.size() * 7) {
                return existentes;
            }
        }

        // Factores por día de la semana (Lun a Dom) para reflejar curva real de panadería sobre el promedio de Python
        double[] factorDia = {0.95, 0.90, 1.00, 1.00, 1.15, 1.35, 1.25};
        List<Sugerencia> resultado = new ArrayList<>();

        for (Producto prod : productos) {
            SugerenciaResponseDto pred = solicitarPrediccionParaProducto(prod.getId());
            double base = pred.getCantidadSugerida() > 0 ? pred.getCantidadSugerida() : 10.0;

            NivelConfianza nivel = NivelConfianza.BAJA;
            try {
                if (pred.getNivelConfianza() != null) {
                    nivel = NivelConfianza.valueOf(pred.getNivelConfianza().toUpperCase());
                }
            } catch (Exception ignored) {}

            for (int i = 0; i < 7; i++) {
                LocalDate fechaDia = lunesSemana.plusDays(i);
                double sugeridaDia = Math.max(1, Math.round(base * factorDia[i]));

                Sugerencia sug = sugerenciaRepository.findByProductoIdAndFecha(prod.getId(), fechaDia)
                        .orElse(Sugerencia.builder()
                                .producto(prod)
                                .fecha(fechaDia)
                                .estado(EstadoSugerencia.PENDIENTE)
                                .build());

                sug.setCantidadSugerida(sugeridaDia);
                sug.setNivelConfianza(nivel);
                sug.setFechaGeneracion(LocalDateTime.now());

                // Si no fue ajustada manualmente antes, la cantidad final iguala a la sugerida
                if (sug.getEstado() != EstadoSugerencia.AJUSTADA) {
                    sug.setCantidadFinal(sugeridaDia);
                }

                resultado.add(sugerenciaRepository.save(sug));
            }
        }

        return resultado;
    }
}