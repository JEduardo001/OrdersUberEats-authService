package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoCreateUserEvent;
import com.SoftwareOrdersUberEats.authService.dto.order.DtoCreateOrder;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.enums.ResultEventEnum;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;

import java.util.UUID;
public interface IAuth {
    @Transactional
    DtoAuth create(DtoCreateUser request);
    DtoAuth get(UUID id);
    @Transactional
    DtoAuth update(UUID id,DtoUpdateAuth request);
    DtoAuthSecurity getByUsername(String username);
    void changeStatusUser(DtoCreateUserEvent data, ResultEventEnum status);
    void verifyUserToCreateOrder(DtoCreateOrder request);
    DtoPageableResponse getAllAuths(int page, int size);
}
