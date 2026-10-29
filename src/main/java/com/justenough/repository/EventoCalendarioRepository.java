package com.justenough.repository;

import com.justenough.model.EventoCalendario;
import com.justenough.model.enums.TipoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Long> {

    // Eventos generales (feriados nacionales), que no pertenecen a ningún comercio
    List<EventoCalendario> findByComercioIsNullAndFechaBetween(LocalDate desde, LocalDate hasta);

    List<EventoCalendario> findByComercioIdAndFechaBetween(Long comercioId, LocalDate desde, LocalDate hasta);

    Optional<EventoCalendario> findByIdAndComercioId(Long id, Long comercioId);

    boolean existsByComercioIsNullAndTipoAndFechaBetween(TipoEvento tipo, LocalDate desde, LocalDate hasta);
}
