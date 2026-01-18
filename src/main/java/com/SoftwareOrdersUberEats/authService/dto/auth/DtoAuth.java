package com.SoftwareOrdersUberEats.authService.dto.auth;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.enums.StatusResourceAuth;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DtoAuth(
        UUID id,
        String username,
        String email,
        StatusResourceAuth status,
        List<DtoRole> roles,
        Instant createdAt,
        Instant disableAt
) {
}
