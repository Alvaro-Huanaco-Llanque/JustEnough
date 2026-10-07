package com.justenough.repository;

import com.justenough.model.RegistroDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroDiarioRepository extends JpaRepository<RegistroDiario, Long> {
}
