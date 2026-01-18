package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.enums.StatusResourceRole;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNotFoundException;
import com.SoftwareOrdersUberEats.authService.interfaces.RoleInterface;
import com.SoftwareOrdersUberEats.authService.mapper.RoleMapper;
import com.SoftwareOrdersUberEats.authService.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService implements RoleInterface {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public RoleEntity getRoleEntity(Long id){
        return roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
    }

    public DtoPageableResponse<DtoRole> getAllRoles(int page, int size){
        Page<RoleEntity> roles = roleRepository.findAll(PageRequest.of(page,size));
        List<DtoRole> dtoRoles = roles.getContent().stream().map(roleMapper::toDto).collect(Collectors.toList());

        return new DtoPageableResponse<DtoRole>(
                roles.getTotalElements(),
                roles.getTotalPages(),
                dtoRoles
        );
    }

    @Override
    public List<RoleEntity> getAllRolesById(List<Long> idRoles){
        return roleRepository.findAllById(idRoles);
    }

    @Override
    public DtoRole get(Long idRole){
        return roleMapper.toDto(getRoleEntity(idRole));
    }

    @Override
    public DtoRole create(DtoCreateRole request){

        if(roleRepository.existsByName(request.name())){
            throw new RoleNameAlreadyInUseException();
        }
        RoleEntity role = roleMapper.toEntityFromCreateDto(request);
        role.setCreatedAt(Instant.now());
        role.setDisableAt( request.status().equals(StatusResourceRole.DELETE) ? Instant.now() : null);

        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public DtoRole update(DtoUpdateRole request){
        RoleEntity role = getRoleEntity(request.id());

        if(roleRepository.existsByNameAndIdNot(request.name(), request.id())){
            throw new RoleNameAlreadyInUseException();
        }

        roleMapper.updateRoleFromDto(request,role);
        role.setDisableAt( role.getStatus().equals(StatusResourceRole.DELETE) ? Instant.now() : null);

        return roleMapper.toDto(roleRepository.save(role));
    }
}
