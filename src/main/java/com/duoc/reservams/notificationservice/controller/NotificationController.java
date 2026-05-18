package com.duoc.reservams.notificationservice.controller;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.dto.NotificationResponseDTO;
import com.duoc.reservams.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador REST para manejar notificaciones
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // lista todas las notificaciones
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> findAll() {
        return ResponseEntity.ok(notificationService.findAll());
    }

    // busca una notificacion por ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    // lista notificaciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    // lista notificaciones por estado
    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponseDTO>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationService.findByStatus(status));
    }

    // lista notificaciones por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationResponseDTO>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(notificationService.findByType(type));
    }

    // crea una notificacion nueva
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> create(@Valid @RequestBody NotificationRequestDTO request) {
        return ResponseEntity.ok(notificationService.create(request));
    }

    // marca una notificacion como enviada
    @PutMapping("/{id}/sent")
    public ResponseEntity<NotificationResponseDTO> markAsSent(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsSent(id));
    }

    // marca una notificacion como fallida
    @PutMapping("/{id}/failed")
    public ResponseEntity<NotificationResponseDTO> markAsFailed(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsFailed(id));
    }

    // elimina una notificacion, util para pruebas
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}