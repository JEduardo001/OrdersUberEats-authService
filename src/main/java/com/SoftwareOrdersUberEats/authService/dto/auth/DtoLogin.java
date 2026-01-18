package com.SoftwareOrdersUberEats.authService.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoLogin {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
