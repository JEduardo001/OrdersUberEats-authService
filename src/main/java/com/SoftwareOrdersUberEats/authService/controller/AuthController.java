package com.SoftwareOrdersUberEats.authService.controller;

import com.SoftwareOrdersUberEats.authService.constant.ApiBase;
import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApi;
import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiLogin;
import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoLogin;
import com.SoftwareOrdersUberEats.authService.dto.auth.DtoUpdateAuth;
import com.SoftwareOrdersUberEats.authService.dto.order.DtoCreateOrder;
import com.SoftwareOrdersUberEats.authService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.authService.security.jwt.JwtService;
import com.SoftwareOrdersUberEats.authService.service.AuthService;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping( ApiBase.apiBase +"auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtService jwtService;
    private final MappedDiagnosticService mappedDiagnosticService;

    @PostMapping("/login")
    public ResponseEntity<DtoResponseApiLogin> login(@Valid @RequestBody DtoLogin request){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        List<String> roles = authenticate.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        System.out.println("roles : " + roles);
        String token = jwtService.createToken(request.getUsername(), roles);

        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApiLogin.builder()
                .status(HttpStatus.OK.value())
                .token(token)
                .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<DtoResponseApi> register(@Valid @RequestBody DtoCreateUser request){
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoResponseApi.builder()
                .status(HttpStatus.CREATED.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Auth created")
                .data(authService.create(request)).build()
        );
    }

    @GetMapping()
    public ResponseEntity<DtoResponseApi> getAllAuths(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(
                DtoResponseApi.builder()
                        .status(HttpStatus.OK.value())
                        .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                        .message("Auths obtained")
                        .data(authService.getAllAuths(page, size))
                        .build()
        );
    }

    @GetMapping("/{idAuth}")
    public ResponseEntity<DtoResponseApi> getAuth(@PathVariable UUID idAuth){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Auth obtained")
                .data(authService.get(idAuth)).build()
        );
    }

    @PutMapping("/{idAuth}")
    public ResponseEntity<DtoResponseApi> updateAuth(@PathVariable UUID idAuth, @Valid @RequestBody DtoUpdateAuth request){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Auth updated")
                .data(authService.update(idAuth,request)).build()
        );
    }

    @PostMapping("/order")
    public ResponseEntity<DtoResponseApiWithoutData> verifyAuthToCreateOrder(@Valid @RequestBody DtoCreateOrder request){
        authService.verifyUserToCreateOrder(request);
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApiWithoutData.builder()
                .status(HttpStatus.OK.value())
                .correlationId(mappedDiagnosticService.getIdCorrelation())
                .message("Order pending to create")
                .build()
        );
    }
}
