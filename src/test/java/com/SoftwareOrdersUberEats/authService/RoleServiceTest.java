package com.SoftwareOrdersUberEats.authService;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNotFoundException;
import com.SoftwareOrdersUberEats.authService.mapper.RoleMapper;
import com.SoftwareOrdersUberEats.authService.repository.RoleRepository;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import com.SoftwareOrdersUberEats.authService.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleMapper roleMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private MappedDiagnosticService mappedDiagnosticService;

    @InjectMocks
    private RoleService roleService;

    private RoleEntity roleEntity;
    private DtoRole dtoRole;

    @BeforeEach
    void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ADMIN");
        roleEntity.setStatus(StatusResourceRoleEnum.ACTIVE);

        dtoRole = new DtoRole(1L,"ADMIN",StatusResourceRoleEnum.ACTIVE,Instant.now(),null);

    }

    @Test
    @DisplayName("Should return pageable roles successfully")
    void getAllRoles_ShouldReturnPageableResponse() {
        Page<RoleEntity> rolesPage = new PageImpl<>(List.of(roleEntity));
        when(roleRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);
        when(roleMapper.toDto(any())).thenReturn(dtoRole);

        DtoPageableResponse<DtoRole> result = roleService.getAllRoles(0, 10);

        assertNotNull(result);
        assertEquals(1, result.totalElements());
        verify(roleRepository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should return role by id")
    void get_ShouldReturnRole_WhenIdExists() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(roleMapper.toDto(roleEntity)).thenReturn(dtoRole);

        DtoRole result = roleService.get(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.name());
    }

    @Test
    @DisplayName("Should throw RoleNotFoundException when role does not exist")
    void get_ShouldThrowException_WhenIdDoesNotExist() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.get(1L));
    }

    @Test
    @DisplayName("Should create role successfully")
    void create_ShouldSucceed_WhenNameIsUnique() {
        DtoCreateRole createDto = new DtoCreateRole("NEW_ROLE", StatusResourceRoleEnum.ACTIVE);
        when(roleRepository.existsByName("NEW_ROLE")).thenReturn(false);
        when(roleMapper.toEntityFromCreateDto(createDto)).thenReturn(roleEntity);
        when(roleMapper.toDto(any())).thenReturn(dtoRole);

        DtoRole result = roleService.create(createDto);

        assertNotNull(result);
        verify(roleRepository).save(any(RoleEntity.class));
    }

    @Test
    @DisplayName("Should throw RoleNameAlreadyInUseException during creation")
    void create_ShouldThrowException_WhenNameAlreadyExists() {
        DtoCreateRole createDto = new DtoCreateRole("ADMIN", StatusResourceRoleEnum.ACTIVE);
        when(roleRepository.existsByName("ADMIN")).thenReturn(true);

        assertThrows(RoleNameAlreadyInUseException.class, () -> roleService.create(createDto));
        verify(roleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update role successfully")
    void update_ShouldSucceed_WhenDataIsValid() {
        DtoUpdateRole updateDto = new DtoUpdateRole(1L, "UPDATED_NAME", StatusResourceRoleEnum.ACTIVE);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(roleRepository.existsByNameAndIdNot("UPDATED_NAME", 1L)).thenReturn(false);
        when(roleMapper.toDto(any())).thenReturn(dtoRole);

        DtoRole result = roleService.update(updateDto);

        assertNotNull(result);
        verify(roleRepository).save(roleEntity);
    }

    @Test
    @DisplayName("Should throw exception when updating to an existing role name")
    void update_ShouldThrowException_WhenNameAlreadyInUseByAnotherId() {
        DtoUpdateRole updateDto = new DtoUpdateRole(1L, "EXISTING_NAME", StatusResourceRoleEnum.ACTIVE);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(roleRepository.existsByNameAndIdNot("EXISTING_NAME", 1L)).thenReturn(true);

        assertThrows(RoleNameAlreadyInUseException.class, () -> roleService.update(updateDto));
        verify(roleRepository, never()).save(any());
    }
}