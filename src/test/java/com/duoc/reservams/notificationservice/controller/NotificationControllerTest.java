package com.duoc.reservams.notificationservice.controller;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.dto.NotificationResponseDTO;
import com.duoc.reservams.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// pruebas unitarias para NotificationController
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void findAll_shouldReturnNotifications() {
        // Given
        when(notificationService.findAll()).thenReturn(List.of(
                buildNotificationResponse(1L, "PENDING"),
                buildNotificationResponse(2L, "SENT")
        ));

        // When
        ResponseEntity<List<NotificationResponseDTO>> response = notificationController.findAll();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(notificationService, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnNotification() {
        // Given
        when(notificationService.findById(1L)).thenReturn(buildNotificationResponse(1L, "PENDING"));

        // When
        ResponseEntity<NotificationResponseDTO> response = notificationController.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("PENDING", response.getBody().getStatus());

        verify(notificationService, times(1)).findById(1L);
    }

    @Test
    void findByUserId_shouldReturnNotifications() {
        // Given
        when(notificationService.findByUserId(1L)).thenReturn(List.of(
                buildNotificationResponse(1L, "PENDING")
        ));

        // When
        ResponseEntity<List<NotificationResponseDTO>> response = notificationController.findByUserId(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getUserId());

        verify(notificationService, times(1)).findByUserId(1L);
    }

    @Test
    void findByStatus_shouldReturnNotifications() {
        // Given
        when(notificationService.findByStatus("SENT")).thenReturn(List.of(
                buildNotificationResponse(1L, "SENT")
        ));

        // When
        ResponseEntity<List<NotificationResponseDTO>> response = notificationController.findByStatus("SENT");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("SENT", response.getBody().get(0).getStatus());

        verify(notificationService, times(1)).findByStatus("SENT");
    }

    @Test
    void findByType_shouldReturnNotifications() {
        // Given
        when(notificationService.findByType("RESERVATION_CREATED")).thenReturn(List.of(
                buildNotificationResponse(1L, "PENDING")
        ));

        // When
        ResponseEntity<List<NotificationResponseDTO>> response =
                notificationController.findByType("RESERVATION_CREATED");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("RESERVATION_CREATED", response.getBody().get(0).getType());

        verify(notificationService, times(1)).findByType("RESERVATION_CREATED");
    }

    @Test
    void create_shouldReturnCreatedNotification() {
        // Given
        NotificationRequestDTO request = buildNotificationRequest();

        when(notificationService.create(request)).thenReturn(buildNotificationResponse(1L, "PENDING"));

        // When
        ResponseEntity<NotificationResponseDTO> response = notificationController.create(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("PENDING", response.getBody().getStatus());

        verify(notificationService, times(1)).create(request);
    }

    @Test
    void markAsSent_shouldReturnSentNotification() {
        // Given
        when(notificationService.markAsSent(1L)).thenReturn(buildNotificationResponse(1L, "SENT"));

        // When
        ResponseEntity<NotificationResponseDTO> response = notificationController.markAsSent(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("SENT", response.getBody().getStatus());

        verify(notificationService, times(1)).markAsSent(1L);
    }

    @Test
    void markAsFailed_shouldReturnFailedNotification() {
        // Given
        when(notificationService.markAsFailed(1L)).thenReturn(buildNotificationResponse(1L, "FAILED"));

        // When
        ResponseEntity<NotificationResponseDTO> response = notificationController.markAsFailed(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("FAILED", response.getBody().getStatus());

        verify(notificationService, times(1)).markAsFailed(1L);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // Given
        doNothing().when(notificationService).delete(1L);

        // When
        ResponseEntity<Void> response = notificationController.delete(1L);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(notificationService, times(1)).delete(1L);
    }

    private NotificationRequestDTO buildNotificationRequest() {
        NotificationRequestDTO request = new NotificationRequestDTO();
        request.setUserId(1L);
        request.setTitle("Reserva creada");
        request.setMessage("Tu reserva fue creada correctamente");
        request.setType("RESERVATION_CREATED");
        return request;
    }

    private NotificationResponseDTO buildNotificationResponse(Long id, String status) {
        return new NotificationResponseDTO(
                id,
                1L,
                "Reserva creada",
                "Tu reserva fue creada correctamente",
                "RESERVATION_CREATED",
                status,
                LocalDateTime.now()
        );
    }
}