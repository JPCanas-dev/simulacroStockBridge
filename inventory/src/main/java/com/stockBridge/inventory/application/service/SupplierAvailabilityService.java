package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.model.SupplierAvailability;
import com.stockBridge.inventory.domain.port.in.SupplierAvailabilityUseCase;
import com.stockBridge.inventory.domain.port.out.SupplierClientPort;
import org.springframework.stereotype.Service;

// Implementacion del caso de uso de consulta al proveedor externo
@Service
public class SupplierAvailabilityService implements SupplierAvailabilityUseCase {

    private final SupplierClientPort supplierClient;

    public SupplierAvailabilityService(SupplierClientPort supplierClient) {
        this.supplierClient = supplierClient;
    }

    @Override
    public SupplierAvailability checkAvailability(String productCode, int quantity, String category) {
        if (productCode == null || productCode.isBlank()) {
            throw new IllegalArgumentException("El codigo del producto es obligatorio");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        return supplierClient.checkAvailability(productCode, quantity, category);
    }
}
