package com.SoftwareOrdersUberEats.authService.dto.events;

import com.SoftwareOrdersUberEats.authService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.authService.enums.typeEvents.TypeEventEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DtoEvent<T> {
    private TypeEventEnum typeEvent;
    private ResultEventEnum resultEvent;
    private T data;
}

