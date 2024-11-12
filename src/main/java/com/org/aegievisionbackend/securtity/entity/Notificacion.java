package com.org.aegievisionbackend.securtity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String mensaje;
    private LocalDateTime fecha;
    @Lob
    private byte[] imageUrl;
}
