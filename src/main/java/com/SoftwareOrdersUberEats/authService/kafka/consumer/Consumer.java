package com.SoftwareOrdersUberEats.authService.kafka.consumer;


import com.SoftwareOrdersUberEats.authService.dto.events.DtoCreateUserEvent;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@AllArgsConstructor
public class Consumer {

    private final AuthService authService;
    private final ObjectMapper objectMapper;


    @KafkaListener(
            topics = "creating.user.response",
            groupId = "users"    )
    public void responseCreateUser(String rawEvent) {

        String json = rawEvent;
        if (rawEvent.startsWith("\"") && rawEvent.endsWith("\"")) {
            json = new ObjectMapper().readValue(rawEvent, String.class);
        }

        DtoEvent<DtoCreateUserEvent> dto = objectMapper.readValue(
                json,
                new TypeReference<DtoEvent<DtoCreateUserEvent>>() {}
        );

        DtoCreateUserEvent user = objectMapper.convertValue(dto.getData(), DtoCreateUserEvent.class);

        authService.changeStatusUser(user, dto.getResultEvent());
    }
}