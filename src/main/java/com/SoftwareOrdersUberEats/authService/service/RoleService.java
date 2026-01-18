package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.interfaces.RoleInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements RoleInterface {

    @Override
    public List<RoleEntity> getAllRolesById(List<Long> idRoles){
        //return roleRepository.findAllById(idRoles);
        return new ArrayList<>();
    }
}
