package com.stockBridge.supplier.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Peticion entrante de comprobacion de disponibilidad
public record AvailabilityCheckRequest(
        @NotBlank(message = "El codigo de producto es obligatorio")
        String productCode,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a cero")
        Integer quantity,

        @NotBlank(message = "La categoria es obligatoria")
        String category
) {
}
