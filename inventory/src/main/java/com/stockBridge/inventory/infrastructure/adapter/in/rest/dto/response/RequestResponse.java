package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

import com.stockBridge.inventory.domain.model.RequestStatus;

import java.time.LocalDateTime;

// DTO de respuesta para solicitud interna
public record RequestResponse(
        Long id,
        UserResponse user,
        ProductResponse product,
        Integer quantity,
        RequestStatus status,
        String observation,
        LocalDateTime createdAt
) {
}
