package com.org.aegievisionbackend.securtity;

import com.org.aegievisionbackend.securtity.entity.Alarma;
import com.org.aegievisionbackend.securtity.entity.Imagen;
import com.org.aegievisionbackend.securtity.entity.Notificacion;
import com.org.aegievisionbackend.securtity.repository.AlarmaRepository;
import com.org.aegievisionbackend.securtity.repository.ImagenRepository;
import com.org.aegievisionbackend.securtity.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private AlarmaRepository alarmaRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    public Notificacion guardarNotificacion(String tipo, String mensaje, String base64String) {
        // Decodificar la imagen en base64 a bytes
        byte[] contenido = Base64.getDecoder().decode(base64String);

        // Crear una nueva instancia de Notificacion
        Notificacion notificacion = new Notificacion();
        notificacion.setImageUrl(contenido); // Guardar los bytes de la imagen
        notificacion.setFecha(LocalDateTime.now()); // Fecha de captura actual
        notificacion.setTipo(tipo); // Establecer el tipo
        notificacion.setMensaje(mensaje); // Establecer el mensaje

        // Guardar la notificación en el repositorio de Notificacion
        return notificacionRepository.save(notificacion);
    }

    // Obtener todas las notificaciones
    public List<Notificacion> obtenerTodasNotificaciones() {
        return notificacionRepository.findAll();
    }

    // Obtener una notificación por su ID
    public Optional<Notificacion> obtenerNotificacionPorId(Long id) {
        return notificacionRepository.findById(id);
    }

    // Actualizar una notificación
    public Notificacion actualizarNotificacion(Long id, Notificacion notificacion) {
        notificacion.setId(id);
        return notificacionRepository.save(notificacion);
    }

    // Eliminar una notificación
    public void eliminarNotificacion(Long id) {
        notificacionRepository.deleteById(id);
    }

    public Alarma guardarAlarma(Alarma alarma) {
        return alarmaRepository.save(alarma);
    }

    public Imagen guardarImagen(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    public Imagen guardarImagenDesdeBase64(String base64String) {
        byte[] contenido = Base64.getDecoder().decode(base64String);
        Imagen imagen = new Imagen();
        imagen.setContenido(contenido); // Guardar los bytes de la imagen
        imagen.setFechaCaptura(LocalDateTime.now()); // Fecha de captura actual
        return imagenRepository.save(imagen);
    }


    // Servicio para obtener imágenes
    public List<Imagen> obtenerImagenes() {
        return imagenRepository.findAll();  // Llama al repositorio para obtener todas las imágenes
    }

    // Servicio para obtener imágenes por fecha
    public List<Imagen> obtenerImagenesPorFecha(LocalDateTime fecha) {
        return imagenRepository.findByFechaCaptura(fecha);  // Filtra imágenes por fecha
    }

    // Servicio para actualizar una imagen
    public Imagen actualizarImagen(Long id, String contenidoBase64) {
        Optional<Imagen> imagenOpt = imagenRepository.findById(id);
        if (imagenOpt.isPresent()) {
            Imagen imagen = imagenOpt.get();
            imagen.setContenido(contenidoBase64.getBytes());  // Actualiza el contenido de la imagen
            return imagenRepository.save(imagen);  // Guarda la imagen actualizada
        }
        return null;  // Si no se encuentra la imagen, retorna null
    }

    // Servicio para eliminar una imagen
    public boolean eliminarImagen(Long id) {
        Optional<Imagen> imagenOpt = imagenRepository.findById(id);
        if (imagenOpt.isPresent()) {
            imagenRepository.delete(imagenOpt.get());  // Elimina la imagen
            return true;
        }
        return false;  // Si no se encuentra la imagen, retorna false
    }

}
