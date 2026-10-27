package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request;

import com.stockBridge.inventory.domain.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO para registro de usuarios
public record RegisterRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El usuario debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "La contrasenia es obligatoria")
        @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
        String password,

        Role role
) {
}
