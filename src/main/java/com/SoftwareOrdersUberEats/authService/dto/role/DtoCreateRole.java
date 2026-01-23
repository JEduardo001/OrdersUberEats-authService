package com.SoftwareOrdersUberEats.authService.dto.role;

import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DtoCreateRole(
        @NotEmpty
       String name,
       @NotNull
       StatusResourceRoleEnum status
) {
}
