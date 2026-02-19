package com.SoftwareOrdersUberEats.authService.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.CORRELATION_HEADER;


@Service
public class Producer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String request, String nameTopic, String correlationId) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(nameTopic, request);

        if (correlationId != null) {
            record.headers().add(CORRELATION_HEADER, correlationId.getBytes());
        }

        kafkaTemplate.send(record);
    }

    public void publisFailedSendEventDlq(String request) {
        kafkaTemplate.send("dev.auth-ms.failed-send-event-dlq.v1", request);
    }

}
