package com.justenough.controller;

import com.justenough.model.EventoCalendario;
import com.justenough.repository.EventoCalendarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoCalendarioController {

    @Autowired
    private EventoCalendarioRepository eventoCalendarioRepository;

    @GetMapping
    public List<EventoCalendario> listar() {
        return eventoCalendarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoCalendario> buscarPorId(@PathVariable Long id) {
        return eventoCalendarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EventoCalendario guardar(@RequestBody EventoCalendario evento) {
        return eventoCalendarioRepository.save(evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!eventoCalendarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventoCalendarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
