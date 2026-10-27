package com.stockBridge.inventory.domain.exception;

// Falla de comunicacion con el proveedor externo
public class ExternalServiceException extends DomainException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
