package com.justenough.repository;

import com.justenough.model.RecetaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaItemRepository extends JpaRepository<RecetaItem, Long> {
    void deleteByProductoId(Long productoId);
}
