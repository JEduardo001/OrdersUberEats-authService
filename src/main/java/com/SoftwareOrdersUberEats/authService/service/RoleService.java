package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNotFoundException;
import com.SoftwareOrdersUberEats.authService.interfaces.IRole;
import com.SoftwareOrdersUberEats.authService.mapper.RoleMapper;
import com.SoftwareOrdersUberEats.authService.repository.RoleRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.MESSAGE_SAVE_ROLE;
import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.MESSAGE_UPDATE_ROLE;

@Service
@AllArgsConstructor
@Slf4j
public class RoleService implements IRole {

    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final MappedDiagnosticService mappedDiagnosticService;

    private RoleEntity getRoleEntity(Long id){
        return roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
    }

    @Override
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
    public List<DtoRole> getAllRolesById(List<Long> idRoles){
        return roleRepository.findAllById(idRoles).stream().map(roleMapper::toDto)
                .collect(Collectors.toList());
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
        role.setDisableAt( request.status().equals(StatusResourceRoleEnum.DELETE) ? Instant.now() : null);

        roleRepository.save(role);
        log.info(MESSAGE_SAVE_ROLE);

        return roleMapper.toDto(role);
    }

    @Override
    public DtoRole update(DtoUpdateRole request){
        RoleEntity role = getRoleEntity(request.id());

        if(roleRepository.existsByNameAndIdNot(request.name(), request.id())){
            throw new RoleNameAlreadyInUseException();
        }

        roleMapper.updateRoleFromDto(request,role);
        role.setDisableAt( role.getStatus().equals(StatusResourceRoleEnum.DISABLED) ? Instant.now() : null);

        RoleEntity updatedRole = roleRepository.save(role);
        log.info(MESSAGE_UPDATE_ROLE);

        return roleMapper.toDto(updatedRole);
    }
}
