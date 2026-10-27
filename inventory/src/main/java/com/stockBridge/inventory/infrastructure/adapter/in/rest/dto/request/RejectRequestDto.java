package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;

// DTO para rechazo de solicitud con motivo
public record RejectRequestDto(
        @NotBlank(message = "El motivo de rechazo es obligatorio")
        String reason
) {
}
