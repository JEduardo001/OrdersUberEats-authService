package com.SoftwareOrdersUberEats.authService;

import com.SoftwareOrdersUberEats.authService.entity.OutboxEventEntity;
import com.SoftwareOrdersUberEats.authService.enums.StatusEventEnum;
import com.SoftwareOrdersUberEats.authService.kafka.producer.Producer;
import com.SoftwareOrdersUberEats.authService.repository.OutboxEventRepository;
import com.SoftwareOrdersUberEats.authService.service.OutboxEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxEventServiceTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;
    @Mock
    private Producer producer;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxEventService outboxEventService;

    @Test
    @DisplayName("Should publish event and set status to SENT when producer succeeds")
    void publishPendingEvents_Success() {
        // Arrange
        OutboxEventEntity event = OutboxEventEntity.builder()
                .id(UUID.randomUUID())
                .payload("{}")
                .nameTopic("test-topic")
                .statusEvent(StatusEventEnum.PENDING)
                .correlationId("corr-1")
                .build();

        when(outboxEventRepository.findAllByStatusEvent(StatusEventEnum.PENDING))
                .thenReturn(List.of(event));

        // Act
        outboxEventService.publishPendingEvents();

        // Assert
        verify(producer).send(any(), any(), any());
        verify(outboxEventRepository).save(argThat(e -> e.getStatusEvent() == StatusEventEnum.SENT));
    }

    @Test
    @DisplayName("Should increment retry count when producer fails")
    void publishPendingEvents_ShouldIncrementRetry_WhenProducerFails() {
        // Arrange
        OutboxEventEntity event = OutboxEventEntity.builder()
                .retryCount(0)
                .statusEvent(StatusEventEnum.PENDING)
                .build();

        when(outboxEventRepository.findAllByStatusEvent(StatusEventEnum.PENDING))
                .thenReturn(List.of(event));
        doThrow(new RuntimeException("Kafka error")).when(producer).send(any(), any(), any());

        // Act
        outboxEventService.publishPendingEvents();

        // Assert
        assertEquals(1, event.getRetryCount());
        verify(outboxEventRepository).save(event);
    }

    @Test
    @DisplayName("Should move to FAILED and send to DLQ after 20 retries")
    void publishPendingEvents_ShouldFail_AfterMaxRetries() {
        // Arrange
        OutboxEventEntity event = OutboxEventEntity.builder()
                .retryCount(21)
                .statusEvent(StatusEventEnum.PENDING)
                .build();

        when(outboxEventRepository.findAllByStatusEvent(StatusEventEnum.PENDING))
                .thenReturn(List.of(event));
        doThrow(new RuntimeException("Kafka error")).when(producer).send(any(), any(), any());

        // Act
        outboxEventService.publishPendingEvents();

        // Assert
        assertEquals(StatusEventEnum.FAILED, event.getStatusEvent());
        verify(producer).publisFailedSendEventDlq(any());
    }
}