package com.SoftwareOrdersUberEats.authService;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;
import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceRoleEnum;
import com.SoftwareOrdersUberEats.authService.service.AuthService;
import com.SoftwareOrdersUberEats.authService.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;


    @Test
    @DisplayName("Should load user details successfully when user exists")
    void loadUserByUsername_Success() {
        // Arrange
        String username = "tester";
        DtoRole role = new DtoRole(1L,"ADMIN",StatusResourceRoleEnum.ACTIVE,Instant.now(),null);
        DtoAuthSecurity authDto = DtoAuthSecurity.builder()
                .username(username)
                .password("encoded_password")
                .roles(List.of(role))
                .build();

        when(authService.getByUsername(username)).thenReturn(authDto);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("encoded_password", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when username is not found")
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        String username = "unknown";
        when(authService.getByUsername(username)).thenReturn(DtoAuthSecurity.builder().build());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () ->
                userDetailsService.loadUserByUsername(username)
        );
    }
}