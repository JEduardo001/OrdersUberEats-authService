package com.SoftwareOrdersUberEats.authService.dto.role;

import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;

public record DtoUpdateRole(
        Long id,
        String name,
        StatusResourceRoleEnum status
) {
}
