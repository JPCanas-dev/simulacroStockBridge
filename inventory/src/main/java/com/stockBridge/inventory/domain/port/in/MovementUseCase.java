package com.stockBridge.inventory.domain.port.in;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import java.util.List;

// Casos de uso de movimientos de inventario
public interface MovementUseCase {
    List<InventoryMovement> getAll();
    List<InventoryMovement> getByProductId(Long productId);
}
