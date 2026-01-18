package com.SoftwareOrdersUberEats.authService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthEmailAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthNotFoundException;
import com.SoftwareOrdersUberEats.authService.exception.auth.AuthUsernameAlreadyInUseException;
import com.SoftwareOrdersUberEats.authService.exception.auth.PasswordDoNotMatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(1)
public class AuthExceptionHandler {

    private ResponseEntity<DtoResponseApiWithoutData> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(DtoResponseApiWithoutData.builder()
                        .status(status.value())
                        .message(message)
                        .build());
    }

    @ExceptionHandler(AuthEmailAlreadyInUseException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleEmailAlreadyInUse(AuthEmailAlreadyInUseException ex){
        return buildResponse(HttpStatus.CONFLICT, "Email already in use");
    }

    @ExceptionHandler(AuthUsernameAlreadyInUseException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleUsernameAlreadyInUse(AuthUsernameAlreadyInUseException ex){
        return buildResponse(HttpStatus.CONFLICT, "Username already in use");
    }

    @ExceptionHandler(AuthNotFoundException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleAuthNotFound(AuthNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "User not found");
    }

    @ExceptionHandler(PasswordDoNotMatchException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleAuthPasswordDoNotMatch(PasswordDoNotMatchException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Passwords do not match");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<DtoResponseApiWithoutData> badCredentialsException(BadCredentialsException ex){
        return buildResponse(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }
}