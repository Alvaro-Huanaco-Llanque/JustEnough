package com.justenough.repository;

import com.justenough.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByComercioId(Long comercioId);

    Optional<Producto> findByIdAndComercioId(Long id, Long comercioId);
}
