package com.SoftwareOrdersUberEats.authService.dto.role;

import com.SoftwareOrdersUberEats.authService.enums.StatusResourceRole;

public record DtoUpdateRole(
        Long id,
        String name,
        StatusResourceRole status
) {
}
