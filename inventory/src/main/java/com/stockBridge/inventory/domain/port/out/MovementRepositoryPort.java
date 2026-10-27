package com.stockBridge.inventory.domain.port.out;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import java.util.List;

// Puerto de salida para persistencia de movimientos
public interface MovementRepositoryPort {
    InventoryMovement save(InventoryMovement movement);
    List<InventoryMovement> findByProductId(Long productId);
    List<InventoryMovement> findAll();
}
