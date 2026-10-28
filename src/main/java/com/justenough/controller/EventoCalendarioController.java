package com.justenough.controller;

import com.justenough.dto.EventoCalendarioDto;
import com.justenough.dto.EventoRequestDto;
import com.justenough.service.EventoCalendarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoCalendarioController {

    @Autowired
    private EventoCalendarioService eventoCalendarioService;

    // Feriados, efemérides y eventos propios entre dos fechas (AAAA-MM-DD, inclusive)
    @GetMapping
    public List<EventoCalendarioDto> listar(@RequestParam LocalDate desde, @RequestParam LocalDate hasta) {
        return eventoCalendarioService.listar(desde, hasta);
    }

    @PostMapping
    public ResponseEntity<EventoCalendarioDto> crear(@Valid @RequestBody EventoRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCalendarioService.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eventoCalendarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
