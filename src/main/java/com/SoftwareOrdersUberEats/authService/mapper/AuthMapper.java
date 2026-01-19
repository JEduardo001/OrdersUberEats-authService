package com.SoftwareOrdersUberEats.authService.mapper;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import com.SoftwareOrdersUberEats.authService.enums.StatusResourceAuth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface AuthMapper {
    DtoAuth toDto(AuthEntity auth);
    AuthEntity toEntity(DtoCreateUser auth);
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DtoUpdateAuth dto, @MappingTarget AuthEntity entity);
    DtoAuthSecurity toDtoSecurity(AuthEntity auth);
}
