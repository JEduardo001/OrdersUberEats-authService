package com.SoftwareOrdersUberEats.authService.mapper;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    DtoRole toDto(RoleEntity role);
    RoleEntity toEntityFromCreateDto(DtoCreateRole request);
    @Mapping(target = "id", ignore = true)
    void updateRoleFromDto(DtoUpdateRole newRole, @MappingTarget RoleEntity actualRole);
}
