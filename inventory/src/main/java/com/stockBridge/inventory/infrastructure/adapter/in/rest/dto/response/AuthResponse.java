package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

// DTO de respuesta con token JWT
public record AuthResponse(
        String token,
        String tokenType
) {
    public AuthResponse(String token) {
        this(token, "Bearer");
    }
}
