package com.stockBridge.inventory.domain.port.out;

import com.stockBridge.inventory.domain.model.SupplierAvailability;

// Puerto de salida para cliente HTTP externo de proveedores
public interface SupplierClientPort {
    SupplierAvailability checkAvailability(String productCode, int quantity, String category);
}
