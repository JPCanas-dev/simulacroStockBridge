package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

// DTO de respuesta para disponibilidad de proveedor
public record AvailabilityResponse(
        String supplierCode,
        boolean available,
        int availableQuantity,
        int estimatedDeliveryDays
) {
}
