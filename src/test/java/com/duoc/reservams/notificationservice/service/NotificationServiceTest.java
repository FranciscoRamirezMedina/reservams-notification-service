package com.duoc.reservams.notificationservice.service;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.dto.NotificationResponseDTO;
import com.duoc.reservams.notificationservice.model.Notification;
import com.duoc.reservams.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// pruebas unitarias para NotificationService
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void create_shouldCreateNotificationWithPendingStatus() {
        // Given
        NotificationRequestDTO request = buildNotificationRequest();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notification = invocation.getArgument(0);
            notification.setId(1L);
            return notification;
        });

        // When
        NotificationResponseDTO response = notificationService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("Reserva creada", response.getTitle());
        assertEquals("Tu reserva fue creada correctamente", response.getMessage());
        assertEquals("RESERVATION_CREATED", response.getType());
        assertEquals("PENDING", response.getStatus());
        assertNotNull(response.getCreatedAt());

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAsSent_shouldUpdateNotificationStatusToSent() {
        // Given
        Notification notification = buildNotification(1L, "PENDING");

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification savedNotification = invocation.getArgument(0);
            return savedNotification;
        });

        // When
        NotificationResponseDTO response = notificationService.markAsSent(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SENT", response.getStatus());

        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markAsFailed_shouldUpdateNotificationStatusToFailed() {
        // Given
        Notification notification = buildNotification(1L, "PENDING");

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification savedNotification = invocation.getArgument(0);
            return savedNotification;
        });

        // When
        NotificationResponseDTO response = notificationService.markAsFailed(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("FAILED", response.getStatus());

        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void findById_shouldThrowException_whenNotificationNotFound() {
        // Given
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.findById(99L)
        );

        // Then
        assertEquals("Notificación no encontrada", exception.getMessage());

        verify(notificationRepository, times(1)).findById(99L);
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void delete_shouldThrowException_whenNotificationNotFound() {
        // Given
        when(notificationRepository.existsById(99L)).thenReturn(false);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.delete(99L)
        );

        // Then
        assertEquals("Notificación no encontrada", exception.getMessage());

        verify(notificationRepository, times(1)).existsById(99L);
        verify(notificationRepository, never()).deleteById(any());
    }

    private NotificationRequestDTO buildNotificationRequest() {
        NotificationRequestDTO request = new NotificationRequestDTO();
        request.setUserId(1L);
        request.setTitle("Reserva creada");
        request.setMessage("Tu reserva fue creada correctamente");
        request.setType("RESERVATION_CREATED");
        return request;
    }

    private Notification buildNotification(Long id, String status) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setUserId(1L);
        notification.setTitle("Reserva creada");
        notification.setMessage("Tu reserva fue creada correctamente");
        notification.setType("RESERVATION_CREATED");
        notification.setStatus(status);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }
}