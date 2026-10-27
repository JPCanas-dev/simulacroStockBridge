package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

import java.math.BigDecimal;

// DTO de respuesta para producto
public record ProductResponse(
        Long id,
        String code,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer stock,
        Boolean active
) {
}
