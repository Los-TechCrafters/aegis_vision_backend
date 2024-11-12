package com.org.aegievisionbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String mensaje;
    private LocalDateTime fechaHora;
    private boolean alarmaActivada;

    // Nuevo campo para el contador de alarmas
    private int contadorAlarmas;

    // Campo para almacenar la foto del sospechoso en formato byte[]
    @Lob
    private byte[] fotoSospechoso;
}
