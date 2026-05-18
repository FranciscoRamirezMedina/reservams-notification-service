package com.duoc.reservams.notificationservice.repository;

import com.duoc.reservams.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository para trabajar con la tabla notifications
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // lista notificaciones de un usuario
    List<Notification> findByUserId(Long userId);

    // lista notificaciones por estado
    List<Notification> findByStatus(String status);

    // lista notificaciones por tipo
    List<Notification> findByType(String type);
}