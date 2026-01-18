package com.SoftwareOrdersUberEats.authService.controller;

import com.SoftwareOrdersUberEats.authService.constant.ApiBase;
import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApi;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(ApiBase.apiBase + "auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping()
    public ResponseEntity<DtoResponseApi> createUser(@Valid @RequestBody DtoCreateUser request){
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoResponseApi.builder()
                .status(HttpStatus.CREATED.value())
                .message("Auth created")
                .data(authService.create(request)).build()
        );
    }

    @GetMapping("/{idAuth}")
    public ResponseEntity<DtoResponseApi> getAuth(@PathVariable UUID idAuth){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .message("Auth obtained")
                .data(authService.get(idAuth)).build()
        );
    }

    @PostMapping("/{idAuth}")
    public ResponseEntity<DtoResponseApi> updateAuth(@PathVariable UUID idAuth, @Valid @RequestBody DtoUpdateAuth request){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .message("Auth updated")
                .data(authService.update(idAuth,request)).build()
        );
    }


}
