package com.SoftwareOrdersUberEats.authService.controller;

import com.SoftwareOrdersUberEats.authService.constant.ApiBase;
import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApi;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoCreateRole;
import com.SoftwareOrdersUberEats.authService.dto.role.DtoUpdateRole;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import com.SoftwareOrdersUberEats.authService.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ApiBase.apiBase + "auth/role")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final MappedDiagnosticService mappedDiagnosticService;


    @GetMapping()
    public ResponseEntity<DtoResponseApi> getAllRoles(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(
                DtoResponseApi.builder()
                        .status(HttpStatus.OK.value())
                        .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                        .message("Roles obtained")
                        .data(roleService.getAllRoles(page, size))
                        .build()
        );
    }

    @GetMapping("/{idRole}")
    public ResponseEntity<DtoResponseApi> getRole(@PathVariable Long idRole){
        return ResponseEntity.ok(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Role obtained")
                .data(roleService.get(idRole)).build()
        );
    }

    @PostMapping()
    public ResponseEntity<DtoResponseApi> createRole(@Valid @RequestBody DtoCreateRole request){
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoResponseApi.builder()
                .status(HttpStatus.CREATED.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Role created")
                .data(roleService.create(request)).build()
        );
    }

    @PutMapping()
    public ResponseEntity<DtoResponseApi> updateRole(@Valid @RequestBody DtoUpdateRole request){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("Role updated")
                .data(roleService.update(request)).build()
        );
    }
}
