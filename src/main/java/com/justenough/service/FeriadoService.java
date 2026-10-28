package com.justenough.service;

import com.justenough.model.EventoCalendario;
import com.justenough.model.enums.TipoEvento;
import com.justenough.repository.EventoCalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Feriados nacionales de Argentina. Se descargan una vez por año desde ArgentinaDatos
// (incluye traslados y días no laborables con fines turísticos) y quedan guardados en
// eventos_calendario sin comercio, así los usan todas las cuentas y la predicción.
@Service
public class FeriadoService {

    private record FeriadoApi(String fecha, String tipo, String nombre) {}

    @Autowired
    private EventoCalendarioRepository eventoCalendarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.feriados.url:https://api.argentinadatos.com/v1/feriados/}")
    private String urlFeriados;

    // Años que se consultaron sin resultado (o con error): no se reintenta hasta pasado un rato
    private final Map<Integer, Instant> consultasFallidas = new ConcurrentHashMap<>();

    public void asegurarAnios(int desde, int hasta) {
        for (int anio = desde; anio <= hasta; anio++) {
            asegurarAnio(anio);
        }
    }

    private synchronized void asegurarAnio(int anio) {
        LocalDate inicio = LocalDate.of(anio, 1, 1);
        LocalDate fin = LocalDate.of(anio, 12, 31);
        if (eventoCalendarioRepository.existsByComercioIsNullAndTipoAndFechaBetween(TipoEvento.FERIADO, inicio, fin)) {
            return;
        }
        Instant fallo = consultasFallidas.get(anio);
        if (fallo != null && fallo.isAfter(Instant.now().minus(30, ChronoUnit.MINUTES))) {
            return;
        }

        try {
            FeriadoApi[] respuesta = restTemplate.getForObject(urlFeriados + anio, FeriadoApi[].class);
            List<EventoCalendario> feriados = respuesta == null ? List.of() : Arrays.stream(respuesta)
                    .map(f -> EventoCalendario.builder()
                            .fecha(LocalDate.parse(f.fecha()))
                            .tipo(TipoEvento.FERIADO)
                            .descripcion(f.nombre())
                            .categoria(f.tipo())
                            .build())
                    .toList();
            if (feriados.isEmpty()) {
                consultasFallidas.put(anio, Instant.now());
                return;
            }
            eventoCalendarioRepository.saveAll(feriados);
            consultasFallidas.remove(anio);
        } catch (Exception e) {
            // Sin internet o con la API caída el calendario sigue andando, solo que sin feriados
            consultasFallidas.put(anio, Instant.now());
            System.err.println("[FeriadoService] No se pudieron obtener los feriados de " + anio + ": " + e.getMessage());
        }
    }
}
