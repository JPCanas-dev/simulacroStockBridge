package com.stockBridge.inventory.domain.exception;

// Violacion de unicidad o duplicidad en registros
public class DuplicateResourceException extends DomainException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
