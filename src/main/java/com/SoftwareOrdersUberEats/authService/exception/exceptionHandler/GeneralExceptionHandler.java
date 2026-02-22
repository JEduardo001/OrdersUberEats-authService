package com.SoftwareOrdersUberEats.authService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.authService.dto.apiResponse.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.authService.exception.infrestructure.InfrastructureUnavailableException;
import com.SoftwareOrdersUberEats.authService.service.MappedDiagnosticService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.ServiceUnavailableException;

import static com.SoftwareOrdersUberEats.authService.constant.TracerConstants.CORRELATION_KEY;

@ControllerAdvice
@Order(3)
@Slf4j
@AllArgsConstructor
public class GeneralExceptionHandler {

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

    // Validaciones de parametros
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleValidationException(MethodArgumentNotValidException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed for request parameters",ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleMissingRequestParam(MissingServletRequestParameterException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required request parameter: " + ex.getParameterName(),ex);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleMissingPathVariable(MissingPathVariableException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Missing required path variable: " + ex.getVariableName(),ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleHttpMessageNotReadable(HttpMessageNotReadableException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request",ex);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<DtoResponseApiWithoutData> CallNotPermittedException(CallNotPermittedException ex){
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "service not available", ex);
    }

    @ExceptionHandler(InfrastructureUnavailableException.class)
    public ResponseEntity<DtoResponseApiWithoutData> InfrastructureUnavailableException(InfrastructureUnavailableException ex){
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), ex);
    }

    // Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DtoResponseApiWithoutData> handleGeneralException(Exception ex){
        ex.printStackTrace();
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",ex);
    }
}
