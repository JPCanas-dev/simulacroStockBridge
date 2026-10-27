package com.stockBridge.supplier.service;

import com.stockBridge.supplier.dto.AvailabilityCheckRequest;
import com.stockBridge.supplier.dto.AvailabilityCheckResponse;
import org.springframework.stereotype.Service;

// Servicio de simulacion de disponibilidad determinista para proveedores
@Service
public class SupplierSimulatorService {

    public AvailabilityCheckResponse check(AvailabilityCheckRequest request) {
        String code = request.productCode().trim().toUpperCase();
        int requestedQty = request.quantity();

        // Calculo determinista basado en el hash del codigo para consistencia
        int hash = Math.abs(code.hashCode());
        int supplierIndex = (hash % 5) + 1;
        String supplierCode = "SUP-00" + supplierIndex;

        // Disponibilidad: 20 a 100 unidades segun el hash
        int supplierStock = 20 + (hash % 81);
        boolean available = supplierStock >= requestedQty;

        // Dias de entrega: 2 a 5 dias habiles
        int deliveryDays = 2 + (hash % 4);

        return new AvailabilityCheckResponse(
                supplierCode,
                available,
                supplierStock,
                deliveryDays
        );
    }
}
