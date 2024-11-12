package com.org.aegievisionbackend.securtity.repository;

import com.org.aegievisionbackend.securtity.entity.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmaRepository extends JpaRepository<Alarma, Long> {}