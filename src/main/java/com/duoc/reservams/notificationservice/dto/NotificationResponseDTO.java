package com.duoc.reservams.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// DTO para responder datos de notificaciones
@Data
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private Long userId;
    private String title;
    private String message;
    private String type;
    private String status;
    private LocalDateTime createdAt;
}