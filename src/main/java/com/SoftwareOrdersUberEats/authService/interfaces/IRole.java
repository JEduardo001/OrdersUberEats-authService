package com.SoftwareOrdersUberEats.authService.interfaces;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;

import java.util.List;

public interface IRole {
    List<DtoRole> getAllRolesById(List<Long> idRoles);
    DtoRole get(Long idRole);
    DtoRole update(DtoUpdateRole request);
    DtoRole create(DtoCreateRole request);
    DtoPageableResponse<DtoRole> getAllRoles(int page, int size);
}
