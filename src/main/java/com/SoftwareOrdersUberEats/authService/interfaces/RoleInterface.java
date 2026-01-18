package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;

import java.util.List;

public interface RoleInterface {
    List<RoleEntity> getAllRolesById(List<Long> idRoles);
}
