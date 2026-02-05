package com.SoftwareOrdersUberEats.authService;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoPageableResponse;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuth;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.events.DtoCreateUserEvent;
import com.SoftwareOrdersUberEats.authService.dto.order.DtoCreateOrder;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import com.SoftwareOrdersUberEats.authService.enums.ResultEventEnum;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceAuthEnum;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthEmailAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthUsernameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.PasswordDoNotMatchException;
import com.SoftwareOrdersUberEats.authService.mapper.AuthMapper;
import com.SoftwareOrdersUberEats.authService.repository.AuthRepository;
import com.SoftwareOrdersUberEats.authService.service.AuthService;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import com.SoftwareOrdersUberEats.authService.service.OutboxEventService;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OutboxEventService outboxEventService;
    @Mock
    private MappedDiagnosticService mappedDiagnosticService;

    @InjectMocks
    private AuthService authService;

    private DtoCreateUser createUserRequest;
    private AuthEntity authEntity;
    private DtoCreateUserEvent dataToCreateUser;

    @BeforeEach
    void setUp() {
        createUserRequest = new DtoCreateUser();
        createUserRequest.setUsername("tester");
        createUserRequest.setEmail("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setConfirmPassword("password123");

        authEntity = new AuthEntity();
        authEntity.setId(UUID.randomUUID());
        authEntity.setUsername("tester");

        dataToCreateUser = new DtoCreateUserEvent();
        dataToCreateUser.setId(UUID.randomUUID());

    }

    @Test
    @DisplayName("Should create user successfully when data is valid")
    void create_ShouldSucceed_WhenDataIsValid() {
        // Arrange
        when(authRepository.existsByUsername(anyString())).thenReturn(false);
        when(authRepository.existsByEmail(anyString())).thenReturn(false);
        when(authMapper.toEntity(any())).thenReturn(authEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(authMapper.toDto(any())).thenReturn(new DtoAuth());
        when(authMapper.toDtoCreateUserEvent(any())).thenReturn(dataToCreateUser);

        // Act
        DtoAuth result = authService.create(createUserRequest);

        // Assert
        assertNotNull(result);
        verify(authRepository, times(1)).save(any(AuthEntity.class));
        verify(outboxEventService, times(1)).saveEvent(any(), eq("creating.user"));
    }

    @Test
    @DisplayName("Should throw exception when passwords do not match")
    void create_ShouldThrowException_WhenPasswordsDoNotMatch() {
        // Arrange
        createUserRequest.setConfirmPassword("wrong_password");

        // Act & Assert
        assertThrows(PasswordDoNotMatchException.class, () -> {
            authService.create(createUserRequest);
        });

        verify(authRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when username is already taken")
    void create_ShouldThrowException_WhenUsernameExists() {
        // Arrange
        when(authRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthUsernameAlreadyInUseException.class, () -> {
            authService.create(createUserRequest);
        });

        verify(authRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email is already in use")
    void create_ShouldThrowException_WhenEmailExists() {
        // Arrange
        when(authRepository.existsByUsername(anyString())).thenReturn(false);
        when(authRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthEmailAlreadyInUseException.class, () -> {
            authService.create(createUserRequest);
        });
    }

    @Test
    @DisplayName("Should return pageable response when fetching all auths")
    void getAllAuths_ShouldReturnPageableResponse() {
        // Arrange
        Page<AuthEntity> page = new PageImpl<>(List.of(authEntity));
        when(authRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(authMapper.toDto(any())).thenReturn(new DtoAuth());

        // Act
        DtoPageableResponse result = authService.getAllAuths(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.totalElements());
        verify(authRepository).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Should return security dto when username exists")
    void getByUsername_ShouldReturnDto_WhenUserExists() {
        // Arrange
        when(authRepository.findByUsername("tester")).thenReturn(Optional.of(authEntity));
        when(authMapper.toDtoSecurity(authEntity)).thenReturn(DtoAuthSecurity.builder().username("tester").build());

        // Act
        DtoAuthSecurity result = authService.getByUsername("tester");

        // Assert
        assertEquals("tester", result.getUsername());
    }

    @Test
    @DisplayName("Should return empty security dto when username does not exist")
    void getByUsername_ShouldReturnEmptyDto_WhenUserDoesNotExist() {
        // Arrange
        when(authRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act
        DtoAuthSecurity result = authService.getByUsername("unknown");

        // Assert
        assertNull(result.getUsername());
    }

    @Test
    @DisplayName("Should return auth dto when id exists")
    void get_ShouldReturnDto_WhenIdExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(authRepository.findById(id)).thenReturn(Optional.of(authEntity));
        when(authMapper.toDto(authEntity)).thenReturn(new DtoAuth());

        // Act
        DtoAuth result = authService.get(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw exception when updating with existing username in another record")
    void update_ShouldThrowException_WhenUsernameAlreadyInUse() {
        // Arrange
        UUID id = UUID.randomUUID();
        DtoUpdateAuth updateRequest = new DtoUpdateAuth();
        updateRequest.setUsername("existingUser");

        when(authRepository.findById(id)).thenReturn(Optional.of(authEntity));
        when(authRepository.existsByUsernameAndIdNot("existingUser", id)).thenReturn(true);

        // Act & Assert
        assertThrows(AuthUsernameAlreadyInUseException.class, () -> authService.update(id, updateRequest));
    }

    @Test
    @DisplayName("Should update status to ACTIVE when result is CREATED")
    void changeStatusUser_ShouldSetStatusToActive_WhenResultIsCreated() {
        // Arrange
        DtoCreateUserEvent eventData = new DtoCreateUserEvent();
        eventData.setId(authEntity.getId());
        when(authRepository.findById(authEntity.getId())).thenReturn(Optional.of(authEntity));

        // Act
        authService.changeStatusUser(eventData, ResultEventEnum.CREATED);

        // Assert
        assertEquals(StatusResourceAuthEnum.ACTIVE, authEntity.getStatus());
        verify(authRepository).save(authEntity);
    }

    @Test
    @DisplayName("Should update status to VALIDATION_ERROR when result is error")
    void changeStatusUser_ShouldSetStatusToValidationError_WhenResultIsError() {
        // Arrange
        DtoCreateUserEvent eventData = new DtoCreateUserEvent();
        eventData.setId(authEntity.getId());
        when(authRepository.findById(authEntity.getId())).thenReturn(Optional.of(authEntity));

        // Act
        authService.changeStatusUser(eventData, ResultEventEnum.VALIDATION_ERROR);

        // Assert
        assertEquals(StatusResourceAuthEnum.VALIDATION_ERROR, authEntity.getStatus());
    }

    @Test
    @DisplayName("Should save outbox event when verifying user for order")
    void verifyUserToCreateOrder_ShouldSaveEvent() {
        // Arrange
        DtoCreateOrder orderRequest = new DtoCreateOrder();
        orderRequest.setIdUser(authEntity.getId());
        when(authRepository.findById(authEntity.getId())).thenReturn(Optional.of(authEntity));
        when(mappedDiagnosticService.getIdCorrelation()).thenReturn("corr-123");

        // Act
        authService.verifyUserToCreateOrder(orderRequest);

        // Assert
        verify(outboxEventService).saveEvent(any(), eq("order.requested"));
    }
}
