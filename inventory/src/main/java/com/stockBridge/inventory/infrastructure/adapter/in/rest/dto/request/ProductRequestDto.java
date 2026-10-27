package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// DTO para creacion y actualizacion de productos
public record ProductRequestDto(
        @NotBlank(message = "El codigo es obligatorio")
        @Size(max = 50, message = "El codigo no puede exceder 50 caracteres")
        String code,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String name,

        String description,

        @NotBlank(message = "La categoria es obligatoria")
        @Size(max = 50, message = "La categoria no puede exceder 50 caracteres")
        String category,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero")
        BigDecimal price,

        @NotNull(message = "La cantidad disponible es obligatoria")
        @Min(value = 0, message = "Las existencias no pueden ser negativas")
        Integer stock
) {
}
