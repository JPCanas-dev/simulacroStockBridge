package com.stockBridge.inventory.domain.exception;

// Excepcion base para errores de negocio
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}
