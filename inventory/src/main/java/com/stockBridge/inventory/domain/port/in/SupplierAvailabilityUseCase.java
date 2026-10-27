package com.stockBridge.inventory.domain.port.in;

import com.stockBridge.inventory.domain.model.SupplierAvailability;

// Caso de uso para consultar disponibilidad con proveedor
public interface SupplierAvailabilityUseCase {
    SupplierAvailability checkAvailability(String productCode, int quantity, String category);
}
