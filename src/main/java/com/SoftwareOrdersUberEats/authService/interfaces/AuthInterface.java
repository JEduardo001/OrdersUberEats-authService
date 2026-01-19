package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface AuthInterface {
    @Transactional
    DtoAuth create(DtoCreateUser request);
    DtoAuth get(UUID id);
    @Transactional
    DtoAuth update(UUID id,DtoUpdateAuth request);
    DtoAuthSecurity getByUsername(String username);
}
