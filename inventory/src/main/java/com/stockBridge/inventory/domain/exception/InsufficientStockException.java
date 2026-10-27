package com.stockBridge.inventory.domain.exception;

// Stock insuficiente para atender la peticion
public class InsufficientStockException extends DomainException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
