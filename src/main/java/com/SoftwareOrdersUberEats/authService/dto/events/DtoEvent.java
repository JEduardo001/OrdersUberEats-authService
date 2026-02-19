package com.SoftwareOrdersUberEats.authService.dto.events;

import com.SoftwareOrdersUberEats.authService.enums.ResultEventEnum;
import com.SoftwareOrdersUberEats.authService.enums.typeEvents.TypeEventEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DtoEvent<T> {
    private TypeEventEnum typeEvent;
    private ResultEventEnum resultEvent;
    private UUID idEvent;
    private String correlationId;
    private T data;
    private Instant createAt;
}

