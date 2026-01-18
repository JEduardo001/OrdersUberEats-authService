package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.entity.AuthEntity;
import com.SoftwareOrdersUberEats.authService.entity.RoleEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<AuthEntity> auth = authService.getByUsername(username);

        if(auth.isEmpty()){
            throw new BadCredentialsException("Bad credentials");
        }

        List<String> rolesList = auth.get().getRoles().stream()
                .map(RoleEntity::getName)
                .toList();

        return User.builder()
                .username(username)
                .password(auth.get().getPassword())
                .roles(rolesList.toArray(new String[0]))
                .build();
    }
}
