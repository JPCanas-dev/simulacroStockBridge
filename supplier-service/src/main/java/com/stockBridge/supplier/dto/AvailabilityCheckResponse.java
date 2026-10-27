package com.stockBridge.supplier.dto;

// Respuesta con datos de disponibilidad del proveedor
public record AvailabilityCheckResponse(
        String supplierCode,
        boolean available,
        int availableQuantity,
        int estimatedDeliveryDays
) {
}
