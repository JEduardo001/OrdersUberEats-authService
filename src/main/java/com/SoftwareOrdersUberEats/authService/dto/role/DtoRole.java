package com.SoftwareOrdersUberEats.authService.dto.role;

import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;

import java.time.Instant;

public record DtoRole(
        Long id,
        String name,
        StatusResourceRoleEnum status,
        Instant createdAt,
        Instant disableAt
) {
}
