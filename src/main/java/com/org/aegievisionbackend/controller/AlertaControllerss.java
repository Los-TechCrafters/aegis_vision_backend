package com.org.aegievisionbackend.controller;

import com.org.aegievisionbackend.entity.Alerta;
import com.org.aegievisionbackend.service.AlertaServices;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaControllerss {
    private final AlertaServices alertaService;
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public AlertaControllerss(AlertaServices alertaService) {
        this.alertaService = alertaService;
    }

    // Alerta de movimiento con contador y notificación en tiempo real
    @PostMapping("/movimiento")
    public ResponseEntity<String> recibirAlerta(@RequestBody Alerta alerta) {
        if (alertaService.isAlarmaActivada() && "movimiento".equals(alerta.getTipo())) {
            alertaService.recibirAlerta(alerta);
            System.out.println("Alerta recibida: " + alerta.getMensaje());

            // Incrementar el contador de alarmas y enviar notificación a los clientes
            alertaService.incrementarContadorAlarmas();
            notificarClientes(alerta);
        }
        return ResponseEntity.ok("Alerta procesada");
    }

    // Subir fotos de sospechosos asociadas a una alerta específica
    @PostMapping(value = "/{alertaId}/fotoSospechoso", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> recibirFotoSospechoso(@PathVariable Long alertaId, @RequestParam("foto") MultipartFile foto) {
        try {
            alertaService.guardarFotoSospechoso(alertaId, foto);
            return ResponseEntity.ok("Foto de sospechoso recibida y almacenada para la alerta ID: " + alertaId);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar la foto");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/apagarAlarma")
    public ResponseEntity<String> apagarAlarma() {
        alertaService.apagarAlarma();
        return ResponseEntity.ok("Alarma desactivada");
    }

    @PutMapping("/activarAlarma")
    public ResponseEntity<String> activarAlarma() {
        alertaService.activarAlarma();
        return ResponseEntity.ok("Alarma activada");
    }

    @GetMapping("/estado")
    public ResponseEntity<Alerta> obtenerEstado() {
        Alerta estado = new Alerta();
        estado.setAlarmaActivada(alertaService.isAlarmaActivada());
        estado.setContadorAlarmas(alertaService.obtenerContadorAlarmas());
        return ResponseEntity.ok(estado);
    }

    @GetMapping("/live")
    public SseEmitter streamLive() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    @GetMapping("/obtenerAlertas")
    public ResponseEntity<List<Alerta>> obtenerAlertas() {
        return ResponseEntity.ok(alertaService.obtenerAlertas());
    }

    // Método para enviar notificaciones a todos los clientes conectados
    private void notificarClientes(Alerta alerta) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(alerta, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}
