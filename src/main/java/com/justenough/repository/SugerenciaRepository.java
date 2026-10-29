package com.justenough.repository;

import com.justenough.model.Sugerencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {
    List<Sugerencia> findByProductoComercioIdAndFechaBetween(Long comercioId, LocalDate desde, LocalDate hasta);
    Optional<Sugerencia> findByProductoIdAndFecha(Long productoId, LocalDate fecha);
}