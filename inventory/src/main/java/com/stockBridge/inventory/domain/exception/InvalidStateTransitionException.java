package com.stockBridge.inventory.domain.exception;

// Estado invalido para la transicion de la solicitud
public class InvalidStateTransitionException extends DomainException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}
