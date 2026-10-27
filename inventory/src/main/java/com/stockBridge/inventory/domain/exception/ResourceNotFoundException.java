package com.stockBridge.inventory.domain.exception;

// Recurso solicitado no encontrado
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
