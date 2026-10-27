package com.stockBridge.inventory.domain.port.out;

import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import java.util.List;
import java.util.Optional;

// Puerto de salida para persistencia de solicitudes
public interface RequestRepositoryPort {
    ProductRequest save(ProductRequest request);
    Optional<ProductRequest> findById(Long id);
    List<ProductRequest> findAll();
    List<ProductRequest> findByUserId(Long userId);
    List<ProductRequest> findByStatus(RequestStatus status);
}
