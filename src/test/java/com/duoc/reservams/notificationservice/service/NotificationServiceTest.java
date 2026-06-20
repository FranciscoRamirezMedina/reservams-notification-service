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
import java.util.List;
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
    void findAll_shouldReturnNotifications() {
        // Given
        when(notificationRepository.findAll()).thenReturn(List.of(
                buildNotification(1L, "PENDING"),
                buildNotification(2L, "SENT")
        ));

        // When
        List<NotificationResponseDTO> response = notificationService.findAll();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());

        verify(notificationRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnNotification_whenExists() {
        // Given
        Notification notification = buildNotification(1L, "PENDING");

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

        // When
        NotificationResponseDTO response = notificationService.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals("PENDING", response.getStatus());

        verify(notificationRepository, times(1)).findById(1L);
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
    void findByUserId_shouldReturnNotifications() {
        // Given
        when(notificationRepository.findByUserId(1L)).thenReturn(List.of(
                buildNotification(1L, "PENDING")
        ));

        // When
        List<NotificationResponseDTO> response = notificationService.findByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getUserId());

        verify(notificationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void findByStatus_shouldReturnNotifications() {
        // Given
        when(notificationRepository.findByStatus("SENT")).thenReturn(List.of(
                buildNotification(1L, "SENT")
        ));

        // When
        List<NotificationResponseDTO> response = notificationService.findByStatus("SENT");

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("SENT", response.get(0).getStatus());

        verify(notificationRepository, times(1)).findByStatus("SENT");
    }

    @Test
    void findByType_shouldReturnNotifications() {
        // Given
        when(notificationRepository.findByType("RESERVATION_CREATED")).thenReturn(List.of(
                buildNotification(1L, "PENDING")
        ));

        // When
        List<NotificationResponseDTO> response = notificationService.findByType("RESERVATION_CREATED");

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("RESERVATION_CREATED", response.get(0).getType());

        verify(notificationRepository, times(1)).findByType("RESERVATION_CREATED");
    }

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
    void markAsSent_shouldThrowException_whenNotificationNotFound() {
        // Given
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.markAsSent(99L)
        );

        // Then
        assertEquals("Notificación no encontrada", exception.getMessage());

        verify(notificationRepository, times(1)).findById(99L);
        verify(notificationRepository, never()).save(any(Notification.class));
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
    void markAsFailed_shouldThrowException_whenNotificationNotFound() {
        // Given
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.markAsFailed(99L)
        );

        // Then
        assertEquals("Notificación no encontrada", exception.getMessage());

        verify(notificationRepository, times(1)).findById(99L);
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void delete_shouldDeleteNotification_whenNotificationExists() {
        // Given
        when(notificationRepository.existsById(1L)).thenReturn(true);

        // When
        notificationService.delete(1L);

        // Then
        verify(notificationRepository, times(1)).existsById(1L);
        verify(notificationRepository, times(1)).deleteById(1L);
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
        verify(notificationRepository, never()).deleteById(anyLong());
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