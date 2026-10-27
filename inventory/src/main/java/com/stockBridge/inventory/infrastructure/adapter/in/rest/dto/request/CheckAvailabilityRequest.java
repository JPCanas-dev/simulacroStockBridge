package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO para solicitar disponibilidad a proveedor
public record CheckAvailabilityRequest(
        @NotBlank(message = "El codigo del producto es obligatorio")
        String productCode,

        @NotNull(message = "La cantidad requerida es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a cero")
        Integer quantity,

        @NotBlank(message = "La categoria es obligatoria")
        String category
) {
}
