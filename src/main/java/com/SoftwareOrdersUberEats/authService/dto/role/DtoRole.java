package com.SoftwareOrdersUberEats.authService.dto.role;

import com.SoftwareOrdersUberEats.authService.enums.StatusResourceRole;

import java.time.Instant;

public record DtoRole(
        Long id,
        String name,
        StatusResourceRole status,
        Instant createdAt,
        Instant disableAt
) {
}
