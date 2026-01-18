package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import com.SoftwareOrdersUberEats.authService.enums.StatusResourceAuth;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthEmailAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthNotFoundException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthUsernameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.PasswordDoNotMatchException;
import com.SoftwareOrdersUberEats.authService.interfaces.AuthInterface;
import com.SoftwareOrdersUberEats.authService.mapper.AuthMapper;
import com.SoftwareOrdersUberEats.authService.repository.AuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService implements AuthInterface {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final RoleService roleService;

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
        authEntity.setStatus(StatusResourceAuth.PENDING_TO_CREATE);
        authEntity.setRoles(new ArrayList<>());
        return authMapper.toDto(authRepository.save(authEntity));
        //lanzar evento
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

        List<RoleEntity> roles = roleService.getAllRolesById(request.getRoles());
        authMapper.updateEntityFromDto(request, authEntity);
        authEntity.setRoles(roles);

        return authMapper.toDto(authRepository.save(authEntity));

    }

}
