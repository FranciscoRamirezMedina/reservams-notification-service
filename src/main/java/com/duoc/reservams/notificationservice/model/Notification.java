package com.duoc.reservams.notificationservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// esta clase representa una notificación del sistema
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    // ID principal de la notificacion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID logico del usuario que recibira la notificacion
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // titulo corto de la notificacion
    @Column(nullable = false, length = 100)
    private String title;

    // Mensaje que verá el usuario
    @Column(nullable = false, length = 255)
    private String message;

    // tipo: RESERVATION_CREATED, PAYMENT_APPROVED, RESERVATION_CANCELLED
    @Column(nullable = false, length = 50)
    private String type;

    // estado: PENDING, SENT o FAILED
    @Column(nullable = false, length = 30)
    private String status;

    // fecha en que se creo la notificacion
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}