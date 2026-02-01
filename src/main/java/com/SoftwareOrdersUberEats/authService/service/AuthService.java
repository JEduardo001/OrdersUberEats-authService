package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoCreateUserEvent;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoEvent;
import com.SoftwareOrdersUberEats.authService.dto.order.DtoCreateOrder;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceAuthEnum;
import com.SoftwareOrdersUberEats.authService.enums.typeEvents.TypeEventEnum;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthEmailAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthNotFoundException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthUsernameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.PasswordDoNotMatchException;
import com.SoftwareOrdersUberEats.authService.interfaces.IAuth;
import com.SoftwareOrdersUberEats.authService.interfaces.IRole;
import com.SoftwareOrdersUberEats.authService.mapper.AuthMapper;
import com.SoftwareOrdersUberEats.authService.mapper.RoleMapper;
import com.SoftwareOrdersUberEats.authService.repository.AuthRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService implements IAuth {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final RoleMapper roleMapper;
    private final IRole iRoleService;
    private final PasswordEncoder passwordEncoder;
    private final OutboxEventService outboxEventService;
    private final MappedDiagnosticService mappedDiagnosticService;


    @Override
    public DtoPageableResponse getAllAuths(int page, int size){
        Page<AuthEntity> pageAuth = authRepository.findAll(PageRequest.of(page,size));
        List<DtoAuth> listAuths = pageAuth.get().map(authMapper::toDto).collect(Collectors.toList());

        return new DtoPageableResponse<>(
                pageAuth.getTotalElements(),
                pageAuth.getTotalPages(),
                listAuths
        );
    }
    @Override
    public DtoAuthSecurity getByUsername(String username){
        Optional<AuthEntity> auth = authRepository.findByUsername(username);
        if(auth.isEmpty()){
            return DtoAuthSecurity.builder()
                    .username(null)
                    .build();
        }
        return authMapper.toDtoSecurity(auth.get());
    }

    @Override
    public DtoAuth get(UUID id){
        return authMapper.toDto(authRepository.findById(id).orElseThrow(AuthNotFoundException::new));
    }

    @Override
    public DtoAuth create(DtoCreateUser request){

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new PasswordDoNotMatchException();
        }

        if(authRepository.existsByUsername(request.getUsername())){
            throw new AuthUsernameAlreadyInUseException();
        }

        if(authRepository.existsByEmail(request.getEmail())){
            throw new AuthEmailAlreadyInUseException();
        }

        AuthEntity authEntity = authMapper.toEntity(request);
        authEntity.setCreatedAt(Instant.now());
        authEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        authEntity.setStatus(StatusResourceAuthEnum.PENDING_TO_CREATE);
        authEntity.setRoles(new ArrayList<>());

        authRepository.save(authEntity);
        log.info(MESSAGE_SAVE_AUTH);

        DtoCreateUserEvent dataToCreateUser = authMapper.toDtoCreateUserEvent(request);
        dataToCreateUser.setId(authEntity.getId());

        //save event
        DtoEvent<Object> event = DtoEvent.builder()
                .data(dataToCreateUser)
                .idEvent(UUID.randomUUID())
                .correlationId(mappedDiagnosticService.getIdCorrelation())
                .resultEvent(ResultEventEnum.CREATED)
                .typeEvent(TypeEventEnum.CREATE).build();

        outboxEventService.saveEvent(event, "creating.user");

        return authMapper.toDto(authEntity);
    }

    @Override
    public DtoAuth update(UUID idAuth, DtoUpdateAuth request){
        AuthEntity authEntity = authRepository.findById(idAuth).orElseThrow(AuthNotFoundException::new);

        if(authRepository.existsByUsernameAndIdNot(request.getUsername(), idAuth)){
            throw new AuthUsernameAlreadyInUseException();
        }

        if(authRepository.existsByEmailAndIdNot(request.getEmail(), idAuth)){
            throw new AuthEmailAlreadyInUseException();
        }

        List<DtoRole> rolesDto = iRoleService.getAllRolesById(request.getRoles());

        authMapper.updateEntityFromDto(request, authEntity);

        authEntity.setDisableAt(request.getStatus().equals(StatusResourceAuthEnum.DISABLE) ? Instant.now() : null);

        List<RoleEntity> rolesEntities = roleMapper.mapDtosToEntities(rolesDto);
        authEntity.setRoles(rolesEntities);

        authRepository.save(authEntity);
        log.info(MESSAGE_UPDATE_AUTH);

        return authMapper.toDto(authEntity);
    }
    @Override
    public void changeStatusUser(DtoCreateUserEvent data, ResultEventEnum status){
        AuthEntity auth = authRepository.findById(data.getId()).orElseThrow(AuthNotFoundException::new);

        if(status.equals(ResultEventEnum.VALIDATION_ERROR)){
            log.info(MESSAGE_DATA_VALIDATION_CHANGE_STATUS_USER_ERROR);
            auth.setStatus(StatusResourceAuthEnum.VALIDATION_ERROR);
        }

        if(status.equals(ResultEventEnum.CREATED)){
            auth.setStatus(StatusResourceAuthEnum.ACTIVE);
        }

        authRepository.save(auth);
        log.info(MESSAGE_CHANGE_STATUS_AUTH, auth.getId());
    }

    @Override
    public void verifyUserToCreateOrder(DtoCreateOrder request){
        authRepository.findById(request.getIdUser()).orElseThrow(AuthNotFoundException::new);

        DtoEvent<Object> event = DtoEvent.builder()
                .data(request)
                .idEvent(UUID.randomUUID())
                .correlationId(mappedDiagnosticService.getIdCorrelation())
                .resultEvent(ResultEventEnum.CREATED)
                .typeEvent(TypeEventEnum.CREATE).build();

        outboxEventService.saveEvent(event, "order.requested");
    }
}
