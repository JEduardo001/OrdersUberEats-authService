package com.SoftwareOrdersUberEats.authService.dto.auth;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.enums.StatusResourceAuth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoUpdateAuth {

    @NotEmpty
    private String username;
    @Email
    private String email;
    @NotNull
    private StatusResourceAuth status;
    @NotNull
    private List<Long> roles;
}