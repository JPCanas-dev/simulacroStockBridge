package com.stockBridge.inventory.infrastructure.adapter.out.external.dto;

// DTO para la peticion HTTP hacia supplier-availability-service
public record SupplierCheckRequest(
        String productCode,
        int quantity,
        String category
) {
}
