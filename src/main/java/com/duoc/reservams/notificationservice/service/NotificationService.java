package com.duoc.reservams.notificationservice.service;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.dto.NotificationResponseDTO;
import com.duoc.reservams.notificationservice.model.Notification;
import com.duoc.reservams.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de notificaciones
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponseDTO> findAll() {
        logger.info("Listando notificaciones");

        return notificationRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NotificationResponseDTO findById(Long id) {
        Notification notification = findNotificationOrThrow(id);

        return toResponseDTO(notification);
    }

    public List<NotificationResponseDTO> findByUserId(Long userId) {
        logger.info("Listando notificaciones del usuario ID {}", userId);

        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> findByStatus(String status) {
        logger.info("Listando notificaciones con estado {}", status);

        return notificationRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> findByType(String type) {
        logger.info("Listando notificaciones de tipo {}", type);

        return notificationRepository.findByType(type)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NotificationResponseDTO create(NotificationRequestDTO request) {
        logger.info("Iniciando creacion de notificacion para usuario ID {}", request.getUserId());

        Notification notification = new Notification();

        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        // toda notificacion nueva parte pendiente
        notification.setStatus("PENDING");

        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);

        logger.info("Notificacion creada correctamente con ID {} para usuario ID {}",
                savedNotification.getId(),
                savedNotification.getUserId());

        return toResponseDTO(savedNotification);
    }

    public NotificationResponseDTO markAsSent(Long id) {
        logger.info("Marcando notificacion ID {} como enviada", id);

        Notification notification = findNotificationOrThrow(id);

        notification.setStatus("SENT");

        Notification updatedNotification = notificationRepository.save(notification);

        logger.info("Notificacion ID {} actualizada a estado SENT", updatedNotification.getId());

        return toResponseDTO(updatedNotification);
    }

    public NotificationResponseDTO markAsFailed(Long id) {
        logger.info("Marcando notificacion ID {} como fallida", id);

        Notification notification = findNotificationOrThrow(id);

        notification.setStatus("FAILED");

        Notification updatedNotification = notificationRepository.save(notification);

        logger.info("Notificacion ID {} actualizada a estado FAILED", updatedNotification.getId());

        return toResponseDTO(updatedNotification);
    }

    public void delete(Long id) {
        logger.info("Iniciando eliminacion de notificacion ID {}", id);

        if (!notificationRepository.existsById(id)) {
            logger.warn("Notificacion no encontrada con ID {}", id);
            throw new RuntimeException("Notificación no encontrada");
        }

        notificationRepository.deleteById(id);

        logger.info("Notificacion ID {} eliminada correctamente", id);
    }

    private Notification findNotificationOrThrow(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Notificacion no encontrada con ID {}", id);
                    return new RuntimeException("Notificación no encontrada");
                });
    }

    // convierte la entidad Notification a DTO de respuesta
    private NotificationResponseDTO toResponseDTO(Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                notification.getCreatedAt()
        );
    }
}