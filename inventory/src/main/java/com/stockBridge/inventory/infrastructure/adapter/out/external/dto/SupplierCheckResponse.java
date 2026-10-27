package com.stockBridge.inventory.infrastructure.adapter.out.external.dto;

// DTO para la respuesta HTTP de supplier-availability-service
public record SupplierCheckResponse(
        String supplierCode,
        boolean available,
        int availableQuantity,
        int estimatedDeliveryDays
) {
}
