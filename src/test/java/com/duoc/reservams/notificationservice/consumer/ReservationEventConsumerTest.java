package com.duoc.reservams.notificationservice.consumer;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.event.ReservationCreatedEvent;
import com.duoc.reservams.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// pruebas unitarias para el consumer de eventos Kafka
@ExtendWith(MockitoExtension.class)
class ReservationEventConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ReservationEventConsumer reservationEventConsumer;

    @Test
    void consumeReservationCreatedEvent_shouldCreateNotification() {
        // Given
        ReservationCreatedEvent event = new ReservationCreatedEvent(
                1L,
                1L,
                1L,
                1L,
                LocalDate.of(2026, 7, 10),
                LocalDate.of(2026, 7, 12),
                new BigDecimal("120000"),
                "PENDING_PAYMENT",
                "Reserva creada correctamente"
        );

        ArgumentCaptor<NotificationRequestDTO> captor =
                ArgumentCaptor.forClass(NotificationRequestDTO.class);

        // When
        reservationEventConsumer.consumeReservationCreatedEvent(event);

        // Then
        verify(notificationService, times(1)).create(captor.capture());

        NotificationRequestDTO request = captor.getValue();

        assertNotNull(request);
        assertEquals(1L, request.getUserId());
        assertEquals("Reserva creada", request.getTitle());
        assertEquals("Tu reserva ID 1 fue creada correctamente y se encuentra pendiente de pago.", request.getMessage());
        assertEquals("RESERVATION_CREATED", request.getType());
    }
}