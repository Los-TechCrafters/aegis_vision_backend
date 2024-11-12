package com.org.aegievisionbackend.service;

import com.org.aegievisionbackend.entity.Alerta;
import com.org.aegievisionbackend.repository.AlertaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AlertaServices {
    private final AlertaRepository alertaRepository;
    private boolean alarmaActivada = true;
    private final AtomicInteger contadorAlarmas = new AtomicInteger(0); // Usamos AtomicInteger para el contador

    public AlertaServices(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    public Alerta recibirAlerta(Alerta alerta) {
        alerta.setFechaHora(LocalDateTime.now());
        return alertaRepository.save(alerta); // Guardar en la base de datos
    }

    public List<Alerta> obtenerAlertas() {
        return alertaRepository.findAll();
    }

    public void apagarAlarma() {
        this.alarmaActivada = false;
    }

    public void activarAlarma() {
        this.alarmaActivada = true;
    }

    public boolean isAlarmaActivada() {
        return alarmaActivada;
    }

    // Método para incrementar el contador de alarmas
    public void incrementarContadorAlarmas() {
        contadorAlarmas.incrementAndGet();
    }

    // Método para obtener el contador de alarmas
    public int obtenerContadorAlarmas() {
        return contadorAlarmas.get();
    }

    // Método para guardar la foto del sospechoso en la entidad Alerta
    public Alerta guardarFotoSospechoso(Long alertaId, MultipartFile foto) throws IOException {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada"));
        alerta.setFotoSospechoso(foto.getBytes());
        return alertaRepository.save(alerta); // Guardar en la base de datos
    }
}
