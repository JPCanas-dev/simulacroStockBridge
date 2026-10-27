package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

// DTO para inicio de sesion
public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @NotBlank(message = "La contrasenia es obligatoria")
        String password
) {
}
