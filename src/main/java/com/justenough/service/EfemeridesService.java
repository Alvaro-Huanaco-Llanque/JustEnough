package com.justenough.service;

import com.justenough.dto.EventoCalendarioDto;
import com.justenough.model.enums.TipoEvento;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

// Fechas especiales que mueven las ventas de una cafetería pero no son feriado.
// No dependen de ninguna API: se calculan para cualquier año.
@Service
public class EfemeridesService {

    // Mismo día todos los años
    private static final Map<MonthDay, String> FIJAS = Map.ofEntries(
            Map.entry(MonthDay.of(2, 14), "San Valentín"),
            Map.entry(MonthDay.of(3, 8), "Día Internacional de la Mujer"),
            Map.entry(MonthDay.of(7, 20), "Día del Amigo"),
            Map.entry(MonthDay.of(9, 11), "Día del Maestro"),
            Map.entry(MonthDay.of(9, 13), "Día Internacional del Chocolate"),
            Map.entry(MonthDay.of(9, 21), "Día del Estudiante y la Primavera"),
            Map.entry(MonthDay.of(10, 1), "Día Internacional del Café"),
            Map.entry(MonthDay.of(10, 11), "Día del Dulce de Leche"),
            Map.entry(MonthDay.of(10, 16), "Día Mundial del Pan"),
            Map.entry(MonthDay.of(11, 10), "Día de la Tradición"),
            Map.entry(MonthDay.of(12, 24), "Nochebuena"),
            Map.entry(MonthDay.of(12, 31), "Fin de año")
    );

    public List<EventoCalendarioDto> entre(LocalDate desde, LocalDate hasta) {
        List<EventoCalendarioDto> resultado = new ArrayList<>();
        for (int anio = desde.getYear(); anio <= hasta.getYear(); anio++) {
            for (EventoCalendarioDto e : delAnio(anio)) {
                if (!e.getFecha().isBefore(desde) && !e.getFecha().isAfter(hasta)) {
                    resultado.add(e);
                }
            }
        }
        resultado.sort(Comparator.comparing(EventoCalendarioDto::getFecha));
        return resultado;
    }

    private List<EventoCalendarioDto> delAnio(int anio) {
        List<EventoCalendarioDto> lista = new ArrayList<>();
        FIJAS.forEach((dia, nombre) -> lista.add(efemeride(dia.atYear(anio), nombre)));

        // Las que caen en un domingo determinado
        lista.add(efemeride(pascua(anio), "Pascua"));
        lista.add(efemeride(tercerDomingo(anio, 6), "Día del Padre"));
        lista.add(efemeride(tercerDomingo(anio, 8), "Día de las Infancias"));
        lista.add(efemeride(tercerDomingo(anio, 10), "Día de la Madre"));
        return lista;
    }

    private static EventoCalendarioDto efemeride(LocalDate fecha, String nombre) {
        return EventoCalendarioDto.builder()
                .fecha(fecha)
                .tipo(TipoEvento.FECHA_ESPECIAL)
                .titulo(nombre)
                .categoria("Efeméride")
                .build();
    }

    private static LocalDate tercerDomingo(int anio, int mes) {
        return LocalDate.of(anio, mes, 1).with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.SUNDAY));
    }

    // Domingo de Pascua (algoritmo de Meeus/Jones/Butcher para el calendario gregoriano)
    static LocalDate pascua(int anio) {
        int a = anio % 19, b = anio / 100, c = anio % 100, d = b / 4, e = b % 4;
        int f = (b + 8) / 25, g = (b - f + 1) / 3, h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4, k = c % 4, l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31;
        int dia = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(anio, mes, dia);
    }
}
