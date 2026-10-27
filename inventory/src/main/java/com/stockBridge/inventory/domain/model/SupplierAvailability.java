package com.stockBridge.inventory.domain.model;

// Respuesta de consulta de disponibilidad al proveedor
public record SupplierAvailability(
        String supplierCode,
        boolean available,
        int availableQuantity,
        int estimatedDeliveryDays
) {
}
