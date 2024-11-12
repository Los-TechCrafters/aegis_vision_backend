package com.org.aegievisionbackend.securtity.capImage;

import com.org.aegievisionbackend.securtity.entity.Alarma;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class CapturaImagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] imagen;

    private LocalDateTime capturaTimestamp;

    @ManyToOne
    @JoinColumn(name = "alarma_id")
    private Alarma alarma;

    // Getters y setters
}
