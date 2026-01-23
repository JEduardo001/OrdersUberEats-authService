package com.SoftwareOrdersUberEats.authService.dto.auth;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceAuthEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoAuthSecurity {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private StatusResourceAuthEnum status;
    private List<DtoRole> roles;
}
