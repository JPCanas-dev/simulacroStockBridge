package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

import com.stockBridge.inventory.domain.model.MovementType;

import java.time.LocalDateTime;

// DTO de respuesta para movimiento de inventario
public record MovementResponse(
        Long id,
        ProductResponse product,
        Long requestId,
        MovementType movementType,
        Integer quantity,
        LocalDateTime createdAt
) {
}
