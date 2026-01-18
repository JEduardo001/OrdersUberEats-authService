package com.SoftwareOrdersUberEats.authService.repository;

import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByUsernameAndIdNot(String username, UUID id);

}
