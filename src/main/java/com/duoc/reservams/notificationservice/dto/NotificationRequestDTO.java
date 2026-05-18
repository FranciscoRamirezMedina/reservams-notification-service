package com.duoc.reservams.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO para crear una notificación
@Data
public class NotificationRequestDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El mensaje es obligatorio")
    private String message;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String type;
}