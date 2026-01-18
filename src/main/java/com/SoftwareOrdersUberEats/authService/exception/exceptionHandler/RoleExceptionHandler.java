package com.SoftwareOrdersUberEats.authService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
@Order(2)
public class RoleExceptionHandler {

    private ResponseEntity<DtoResponseApiWithoutData> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(DtoResponseApiWithoutData.builder()
                        .status(status.value())
                        .message(message)
                        .build());
    }

    @ExceptionHandler(RoleNameAlreadyInUseException.class)
    public ResponseEntity<DtoResponseApiWithoutData> roleNameAlreadyInUse(RoleNameAlreadyInUseException ex){
        return buildResponse(HttpStatus.CONFLICT, "Role name already in use");
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<DtoResponseApiWithoutData> roleNotFoundException(RoleNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Role not found");
    }
}
