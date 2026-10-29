package com.justenough.service;

import com.justenough.dto.EventoCalendarioDto;
import com.justenough.dto.EventoRequestDto;
import com.justenough.exception.NegocioException;
import com.justenough.model.Comercio;
import com.justenough.model.EventoCalendario;
import com.justenough.model.enums.TipoEvento;
import com.justenough.repository.EventoCalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Calendario del comercio: feriados nacionales + efemérides + eventos propios
@Service
public class EventoCalendarioService {

    private static final long MAX_DIAS_CONSULTA = 400;
    private static final ZoneId ZONA_ARGENTINA = ZoneId.of("America/Argentina/Buenos_Aires");

    @Autowired
    private EventoCalendarioRepository eventoCalendarioRepository;

    @Autowired
    private FeriadoService feriadoService;

    @Autowired
    private EfemeridesService efemeridesService;

    @Autowired
    private SesionService sesionService;

    public List<EventoCalendarioDto> listar(LocalDate desde, LocalDate hasta) {
        if (hasta.isBefore(desde)) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "La fecha hasta no puede ser anterior a la fecha desde.");
        }
        if (ChronoUnit.DAYS.between(desde, hasta) > MAX_DIAS_CONSULTA) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "Se pueden consultar hasta " + MAX_DIAS_CONSULTA + " días por vez.");
        }
        Comercio comercio = sesionService.getComercioActual();
        feriadoService.asegurarAnios(desde.getYear(), hasta.getYear());

        List<EventoCalendarioDto> eventos = new ArrayList<>();
        eventoCalendarioRepository.findByComercioIsNullAndFechaBetween(desde, hasta)
                .forEach(e -> eventos.add(toDto(e, false)));
        eventoCalendarioRepository.findByComercioIdAndFechaBetween(comercio.getId(), desde, hasta)
                .forEach(e -> eventos.add(toDto(e, true)));
        eventos.addAll(efemeridesService.entre(desde, hasta));

        // En cada día: primero los feriados, después las efemérides y al final los eventos propios
        eventos.sort(Comparator.comparing(EventoCalendarioDto::getFecha)
                .thenComparing(e -> orden(e.getTipo())));
        return eventos;
    }

    public EventoCalendarioDto crear(EventoRequestDto dto) {
        if (dto.getFecha().isBefore(LocalDate.now(ZONA_ARGENTINA))) {
            throw new NegocioException(HttpStatus.BAD_REQUEST, "No se pueden agregar eventos en fechas que ya pasaron.");
        }
        Comercio comercio = sesionService.getComercioActual();
        String categoria = dto.getCategoria() == null || dto.getCategoria().isBlank() ? "Otro" : dto.getCategoria().trim();
        EventoCalendario evento = EventoCalendario.builder()
                .comercio(comercio)
                .fecha(dto.getFecha())
                .tipo(TipoEvento.EVENTO_LOCAL)
                .descripcion(dto.getNombre())
                .categoria(categoria)
                .impacto(dto.getImpacto())
                .build();
        return toDto(eventoCalendarioRepository.save(evento), true);
    }

    // Solo se pueden borrar los eventos propios (los feriados son de todos)
    public void eliminar(Long id) {
        Comercio comercio = sesionService.getComercioActual();
        EventoCalendario evento = eventoCalendarioRepository.findByIdAndComercioId(id, comercio.getId())
                .orElseThrow(() -> NegocioException.noEncontrado("No existe ese evento en tu calendario."));
        eventoCalendarioRepository.delete(evento);
    }

    // Días con feriado nacional o con un evento propio del comercio (lo usa la predicción)
    public Set<LocalDate> fechasConFeriadoOEvento(Long comercioId, LocalDate desde, LocalDate hasta) {
        feriadoService.asegurarAnios(desde.getYear(), hasta.getYear());
        return Stream.concat(
                        eventoCalendarioRepository.findByComercioIsNullAndFechaBetween(desde, hasta).stream(),
                        eventoCalendarioRepository.findByComercioIdAndFechaBetween(comercioId, desde, hasta).stream())
                .map(EventoCalendario::getFecha)
                .collect(Collectors.toSet());
    }

    private static int orden(TipoEvento tipo) {
        return switch (tipo) {
            case FERIADO -> 0;
            case FECHA_ESPECIAL -> 1;
            case EVENTO_LOCAL -> 2;
        };
    }

    private static EventoCalendarioDto toDto(EventoCalendario e, boolean propio) {
        return EventoCalendarioDto.builder()
                .id(e.getId())
                .fecha(e.getFecha())
                .tipo(e.getTipo())
                .titulo(e.getDescripcion())
                .categoria(e.getCategoria())
                .impacto(e.getImpacto())
                .propio(propio)
                .build();
    }
}
