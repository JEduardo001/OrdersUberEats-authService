package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoCreateUserEvent;
import com.SoftwareOrdersUberEats.authService.dto.order.DtoCreateOrder;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.enums.statesCreateResource.ResultEventEnum;
import jakarta.transaction.Transactional;

import java.util.UUID;

public interface AuthInterface {
    @Transactional
    DtoAuth create(DtoCreateUser request);
    DtoAuth get(UUID id);
    @Transactional
    DtoAuth update(UUID id,DtoUpdateAuth request);
    DtoAuthSecurity getByUsername(String username);
    void changeStatusUser(DtoCreateUserEvent data, ResultEventEnum status);
    void verifyUserToCreateOrder(DtoCreateOrder request);
}
