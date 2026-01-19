package com.SoftwareOrdersUberEats.authService.service;

import com.SoftwareOrdersUberEats.authService.dto.auth.DtoAuthSecurity;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoRole;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username){
        DtoAuthSecurity auth = authService.getByUsername(username);

        if(auth.getUsername() == null){
            throw new BadCredentialsException("Bad credentials");
        }

        List<String> rolesList = auth.getRoles().stream()
                .map(DtoRole::name)
                .toList();

        return User.builder()
                .username(username)
                .password(auth.getPassword())
                .roles(rolesList.toArray(new String[0]))
                .build();
    }
}
