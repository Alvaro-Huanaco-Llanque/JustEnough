package com.justenough.repository;

import com.justenough.model.Ajuste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AjusteRepository extends JpaRepository<Ajuste, Long> {
}
