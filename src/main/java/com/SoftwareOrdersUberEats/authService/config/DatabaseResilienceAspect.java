package com.SoftwareOrdersUberEats.authService.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DatabaseResilienceAspect {

    private final CircuitBreakerRegistry registry;

    public DatabaseResilienceAspect(CircuitBreakerRegistry registry) {
        this.registry = registry;
    }

    @Around("execution(* com.SoftwareOrdersUberEats.authService.repository.*.*(..))")
    public Object circuitBreakerAround(ProceedingJoinPoint freePart) throws Throwable {
        String name = freePart.getSignature().getDeclaringType().getSimpleName(); //tomar el nombre de la clase que se llamo
        CircuitBreaker breaker = registry.circuitBreaker(name, "dbConfig");// tomamos la conf dbconfig del circuit bracker

        return breaker.executeCheckedSupplier(freePart::proceed);
    }
}