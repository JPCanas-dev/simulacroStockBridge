package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.domain.port.in.MovementUseCase;
import com.stockBridge.inventory.domain.port.out.MovementRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implementacion de casos de uso de movimientos de inventario
@Service
@Transactional(readOnly = true)
public class MovementService implements MovementUseCase {

    private final MovementRepositoryPort movementRepository;

    public MovementService(MovementRepositoryPort movementRepository) {
        this.movementRepository = movementRepository;
    }

    @Override
    public List<InventoryMovement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    public List<InventoryMovement> getByProductId(Long productId) {
        return movementRepository.findByProductId(productId);
    }
}
