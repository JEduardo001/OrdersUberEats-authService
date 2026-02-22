package com.SoftwareOrdersUberEats.authService.exception.infrestructure;

public class InfrastructureUnavailableException extends RuntimeException{
    public InfrastructureUnavailableException(String message){
        super(message);
    }
}
