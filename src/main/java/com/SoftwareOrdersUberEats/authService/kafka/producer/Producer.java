package com.SoftwareOrdersUberEats.authService.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String request,String nameTopic){
        kafkaTemplate.send(nameTopic, request);
    }

    public void publisFailedSendEventDlq(String request) {
        kafkaTemplate.send("failed.send.event.dlq", request);
    }

}
