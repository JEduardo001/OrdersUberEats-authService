package com.SoftwareOrdersUberEats.authService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.role.RoleNotFoundException;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.CORRELATION_KEY;

@ControllerAdvice
@Order(2)
@Slf4j
@AllArgsConstructor
public class RoleExceptionHandler {

    private final MappedDiagnosticService mappedDiagnosticService;

    private ResponseEntity<DtoResponseApiWithoutData> buildResponse(HttpStatus status, String message, Exception ex) {
        log.warn("Business exception: {} - Message: {}", ex.getClass().getSimpleName(), message);

        return ResponseEntity.status(status)
                .body(DtoResponseApiWithoutData.builder()
                        .status(status.value())
                        .message(message)
                        .correlationId(mappedDiagnosticService.getIdCorrelation())
                        .build());
    }

    @ExceptionHandler(RoleNameAlreadyInUseException.class)
    public ResponseEntity<DtoResponseApiWithoutData> roleNameAlreadyInUse(RoleNameAlreadyInUseException ex){
        return buildResponse(HttpStatus.CONFLICT, "Role name already in use",ex);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<DtoResponseApiWithoutData> roleNotFoundException(RoleNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Role not found",ex);
    }
}
