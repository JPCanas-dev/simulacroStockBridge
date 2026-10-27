package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

import com.stockBridge.inventory.domain.model.Role;

// DTO de respuesta para usuario registrado
public record UserResponse(
        Long id,
        String username,
        Role role
) {
}
