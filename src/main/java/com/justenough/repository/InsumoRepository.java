package com.justenough.repository;

import com.justenough.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    
    // Acá podés agregar métodos personalizados de búsqueda más adelante si los necesitás.
    // Por ejemplo: List<Insumo> findByComercioId(Long comercioId);
}
