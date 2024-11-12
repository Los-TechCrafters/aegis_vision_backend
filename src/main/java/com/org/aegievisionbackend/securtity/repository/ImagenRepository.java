package com.org.aegievisionbackend.securtity.repository;

import com.org.aegievisionbackend.securtity.entity.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    List<Imagen> findByFechaCaptura(LocalDateTime fechaCaptura);
}
