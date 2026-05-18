package com.duoc.reservams.notificationservice.service;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.dto.NotificationResponseDTO;
import com.duoc.reservams.notificationservice.model.Notification;
import com.duoc.reservams.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de notificaciones
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationResponseDTO> findAll() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NotificationResponseDTO findById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        return toResponseDTO(notification);
    }

    public List<NotificationResponseDTO> findByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> findByStatus(String status) {
        return notificationRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> findByType(String type) {
        return notificationRepository.findByType(type)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NotificationResponseDTO create(NotificationRequestDTO request) {
        Notification notification = new Notification();

        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        // toda notificacion nueva parte pendiente
        notification.setStatus("PENDING");

        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification = notificationRepository.save(notification);

        return toResponseDTO(savedNotification);
    }

    public NotificationResponseDTO markAsSent(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notification.setStatus("SENT");

        Notification updatedNotification = notificationRepository.save(notification);

        return toResponseDTO(updatedNotification);
    }

    public NotificationResponseDTO markAsFailed(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notification.setStatus("FAILED");

        Notification updatedNotification = notificationRepository.save(notification);

        return toResponseDTO(updatedNotification);
    }

    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }

        notificationRepository.deleteById(id);
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