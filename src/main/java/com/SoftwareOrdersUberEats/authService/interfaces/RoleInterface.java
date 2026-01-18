package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;

import java.util.List;

public interface RoleInterface {
    List<RoleEntity> getAllRolesById(List<Long> idRoles);
    DtoRole get(Long idRole);
    DtoRole update(DtoUpdateRole request);
    DtoRole create(DtoCreateRole request);
}
