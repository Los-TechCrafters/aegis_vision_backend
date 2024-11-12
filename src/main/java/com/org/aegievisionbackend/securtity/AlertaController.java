package com.org.aegievisionbackend.securtity;

import com.org.aegievisionbackend.securtity.entity.Alarma;
import com.org.aegievisionbackend.securtity.entity.Imagen;
import com.org.aegievisionbackend.securtity.entity.Notificacion;
import com.org.aegievisionbackend.securtity.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping("/notificacion")
    public ResponseEntity<Notificacion> recibirNotificacion(@RequestBody Map<String, Object> imagenData) {
        try {
            String contenidoBase64 = (String) imagenData.get("contenido");
            String tipo = (String) imagenData.get("tipo");
            String mensaje = (String) imagenData.get("mensaje");

            // Usamos el método 'guardarNotificacion' del servicio
            Notificacion guardada = alertaService.guardarNotificacion(tipo, mensaje, contenidoBase64);
            return ResponseEntity.ok(guardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para obtener todas las notificaciones
    @GetMapping("/obtener-notification")
    public ResponseEntity<List<Notificacion>> obtenerTodasNotificaciones() {
        List<Notificacion> notificaciones = alertaService.obtenerTodasNotificaciones();
        return ResponseEntity.ok(notificaciones);
    }

    // Endpoint para obtener una notificación por ID
    @GetMapping("/notification/{id}")
    public ResponseEntity<Notificacion> obtenerNotificacionPorId(@PathVariable Long id) {
        Optional<Notificacion> notificacion = alertaService.obtenerNotificacionPorId(id);
        return notificacion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para actualizar una notificación
    @PutMapping("/notification/{id}")
    public ResponseEntity<Notificacion> actualizarNotificacion(@PathVariable Long id, @RequestBody Notificacion notificacion) {
        if (alertaService.obtenerNotificacionPorId(id).isPresent()) {
            Notificacion notificacionActualizada = alertaService.actualizarNotificacion(id, notificacion);
            return ResponseEntity.ok(notificacionActualizada);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para eliminar una notificación
    @DeleteMapping("/notification/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        if (alertaService.obtenerNotificacionPorId(id).isPresent()) {
            alertaService.eliminarNotificacion(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/alarma")
    public ResponseEntity<Alarma> recibirAlarma(@RequestBody Alarma alarma) {
        Alarma guardada = alertaService.guardarAlarma(alarma);
        return ResponseEntity.ok(guardada);
    }

    @PostMapping("/imagen")
    public ResponseEntity<Imagen> recibirImagen(@RequestBody Map<String, Object> imagenData) {
        try {
            String contenidoBase64 = (String) imagenData.get("contenido");

            // Usamos el método 'guardarImagenDesdeBase64' del servicio
            Imagen guardada = alertaService.guardarImagenDesdeBase64(contenidoBase64);
            return ResponseEntity.ok(guardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/imagen")
    public ResponseEntity<List<Imagen>> obtenerImagenes() {
        List<Imagen> imagenes = alertaService.obtenerImagenes();  // Aquí llamas a tu servicio para obtener las imágenes
        if (imagenes.isEmpty()) {
            return ResponseEntity.noContent().build();  // Si no hay imágenes, devuelve un 204
        }
        return ResponseEntity.ok(imagenes);  // Devuelve un 200 con las imágenes
    }

    @GetMapping("/imagen-conFecha")
    public ResponseEntity<List<Imagen>> obtenerImagenesConFecha(
            @RequestParam(value = "fecha", required = false)
            String fecha) {

        LocalDateTime fechaCaptura = null;
        if (fecha != null) {
            // Convierte el parámetro de cadena a LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            fechaCaptura = LocalDateTime.parse(fecha, formatter);
        }

        List<Imagen> imagenes = alertaService.obtenerImagenesPorFecha(fechaCaptura);
        return ResponseEntity.ok(imagenes);
    }

    @PutMapping("/imagen/{id}")
    public ResponseEntity<Imagen> actualizarImagen(@PathVariable Long id, @RequestBody Map<String, Object> imagenData) {
        try {
            String contenidoBase64 = (String) imagenData.get("contenido");
            Imagen imagenActualizada = alertaService.actualizarImagen(id, contenidoBase64);
            if (imagenActualizada == null) {
                return ResponseEntity.notFound().build();  // Si no se encuentra la imagen, devuelve 404
            }
            return ResponseEntity.ok(imagenActualizada);  // Devuelve la imagen actualizada
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();  // Si ocurre un error en el proceso, devuelve 400
        }
    }

    @DeleteMapping("/imagen/{id}")
    public ResponseEntity<Void> eliminarImagen(@PathVariable Long id) {
        boolean eliminado = alertaService.eliminarImagen(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();  // Si no se encuentra la imagen, devuelve 404
        }
        return ResponseEntity.noContent().build();  // Si la eliminación fue exitosa, devuelve 204
    }


}
