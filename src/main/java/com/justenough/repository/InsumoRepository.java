package com.justenough.repository;

import com.justenough.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    List<Insumo> findByComercioId(Long comercioId);

    Optional<Insumo> findByIdAndComercioId(Long id, Long comercioId);

    Optional<Insumo> findByNombreAndComercioId(String nombre, Long comercioId);
}
