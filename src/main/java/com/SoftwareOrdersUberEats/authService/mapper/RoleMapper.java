package com.SoftwareOrdersUberEats.authService.mapper;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    DtoRole toDto(RoleEntity role);
}
