package com.justenough.controller;

import com.justenough.model.Sugerencia;
import com.justenough.model.enums.EstadoSugerencia;
import com.justenough.repository.SugerenciaRepository;
import com.justenough.service.PredictivoService;
import com.justenough.service.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sugerencias")
@CrossOrigin(origins = "*")
public class SugerenciaController {

    @Autowired
    private SugerenciaRepository sugerenciaRepository;

    @Autowired
    private PredictivoService predictivoService;

    @Autowired
    private SesionService sesionService;

    @GetMapping
    public List<Sugerencia> listar() {
        return sugerenciaRepository.findAll();
    }

    // Devuelve las sugerencias de la semana actual (y si no existen, consulta a Python y las crea)
    @GetMapping("/semana")
    public ResponseEntity<List<Sugerencia>> obtenerSemana(
            @RequestParam(required = false) String desde,
            @RequestParam(defaultValue = "false") boolean regenerar) {
        LocalDate lunes = (desde != null && !desde.isBlank())
                ? LocalDate.parse(desde)
                : LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<Sugerencia> semana = predictivoService.obtenerOGenerarSemana(lunes, regenerar);
        return ResponseEntity.ok(semana);
    }

    // Ajustar la cantidad de un día específico
    @PutMapping("/{id}/ajustar")
    @Transactional
    public ResponseEntity<Sugerencia> ajustarSugerencia(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Sugerencia sug = sugerenciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sugerencia no encontrada"));

        double nuevaCantidad = Double.parseDouble(body.get("nuevaCantidad").toString());
        sug.setCantidadFinal(nuevaCantidad);
        sug.setEstado(EstadoSugerencia.AJUSTADA);
        return ResponseEntity.ok(sugerenciaRepository.save(sug));
    }

    // Aceptar toda la fila (los 7 días) de un producto en esa semana
    @PutMapping("/producto/{productoId}/aceptar")
    @Transactional
    public ResponseEntity<?> aceptarProductoSemana(
            @PathVariable Long productoId,
            @RequestParam String desde) {
        LocalDate lunes = LocalDate.parse(desde);
        LocalDate domingo = lunes.plusDays(6);
        Long comercioId = sesionService.getComercioActual().getId();

        List<Sugerencia> lista = sugerenciaRepository.findByProductoComercioIdAndFechaBetween(comercioId, lunes, domingo);
        for (Sugerencia s : lista) {
            if (s.getProducto().getId().equals(productoId) && s.getEstado() == EstadoSugerencia.PENDIENTE) {
                s.setEstado(EstadoSugerencia.ACEPTADA);
                sugerenciaRepository.save(s);
            }
        }
        return ResponseEntity.ok("{\"message\": \"Sugerencia del producto aceptada\"}");
    }

    // Aceptar todas las sugerencias pendientes de la semana
    @PutMapping("/aceptar-todas")
    @Transactional
    public ResponseEntity<?> aceptarTodas(@RequestParam String desde) {
        LocalDate lunes = LocalDate.parse(desde);
        LocalDate domingo = lunes.plusDays(6);
        Long comercioId = sesionService.getComercioActual().getId();

        List<Sugerencia> lista = sugerenciaRepository.findByProductoComercioIdAndFechaBetween(comercioId, lunes, domingo);
        for (Sugerencia s : lista) {
            if (s.getEstado() == EstadoSugerencia.PENDIENTE) {
                s.setEstado(EstadoSugerencia.ACEPTADA);
                sugerenciaRepository.save(s);
            }
        }
        return ResponseEntity.ok("{\"message\": \"Todas las sugerencias fueron aceptadas\"}");
    }
}