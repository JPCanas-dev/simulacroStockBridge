package com.stockBridge.inventory.domain.exception;

// Acceso denegado a nivel de regla de negocio
public class UnauthorizedAccessException extends DomainException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
