package com.duoc.reservams.notificationservice.consumer;

import com.duoc.reservams.notificationservice.dto.NotificationRequestDTO;
import com.duoc.reservams.notificationservice.event.ReservationCreatedEvent;
import com.duoc.reservams.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// clase encargada de consumir eventos de reservas desde Kafka
@Component
public class ReservationEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ReservationEventConsumer.class);

    private final NotificationService notificationService;

    public ReservationEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // escucha el evento cuando se crea una reserva
    @KafkaListener(topics = "reservation-created-topic", groupId = "notification-service-group")
    public void consumeReservationCreatedEvent(ReservationCreatedEvent event) {
        logger.info("Evento de reserva recibido desde Kafka. reservationId={}", event.getReservationId());

        NotificationRequestDTO request = new NotificationRequestDTO();
        request.setUserId(event.getClientUserId());
        request.setTitle("Reserva creada");
        request.setMessage("Tu reserva ID " + event.getReservationId() + " fue creada correctamente y se encuentra pendiente de pago.");
        request.setType("RESERVATION_CREATED");

        notificationService.create(request);

        logger.info("Notificacion creada desde evento Kafka para usuario ID {}", event.getClientUserId());
    }
}