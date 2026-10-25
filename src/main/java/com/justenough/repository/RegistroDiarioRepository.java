package com.justenough.repository;

import com.justenough.model.RegistroDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {
    Optional<RegistroDiario> findByProductoIdAndFecha(Long productoId, LocalDate fecha);

    List<RegistroDiario> findByProductoComercioId(Long comercioId);

    Optional<RegistroDiario> findByIdAndProductoComercioId(Long id, Long comercioId);

    List<RegistroDiario> findByProductoComercioIdAndFecha(Long comercioId, LocalDate fecha);

    boolean existsByProductoId(Long productoId);
}
