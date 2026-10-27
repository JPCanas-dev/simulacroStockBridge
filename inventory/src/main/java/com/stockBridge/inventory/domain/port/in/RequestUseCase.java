package com.stockBridge.inventory.domain.port.in;

import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import java.util.List;

// Casos de uso de solicitudes
public interface RequestUseCase {
    ProductRequest create(Long userId, Long productId, int quantity, String observation);
    ProductRequest approve(Long requestId);
    ProductRequest reject(Long requestId, String reason);
    ProductRequest deliver(Long requestId);
    ProductRequest cancel(Long requestId, Long currentUserId);
    ProductRequest getById(Long id, Long currentUserId, boolean isAdminOrBodega);
    List<ProductRequest> getAll(Long currentUserId, boolean isAdminOrBodega);
    List<ProductRequest> getByStatus(RequestStatus status);
}
