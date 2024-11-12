package com.org.aegievisionbackend.repository;

import com.org.aegievisionbackend.entity.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    // Aquí puedes agregar consultas personalizadas si lo necesitas
}
