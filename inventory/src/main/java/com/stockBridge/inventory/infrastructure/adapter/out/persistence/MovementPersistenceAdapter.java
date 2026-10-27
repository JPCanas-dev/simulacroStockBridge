package com.stockBridge.inventory.infrastructure.adapter.out.persistence;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.domain.port.out.MovementRepositoryPort;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.MovementEntity;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper.MovementPersistenceMapper;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository.MovementJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

// Adaptador de persistencia para movimientos de inventario
@Component
public class MovementPersistenceAdapter implements MovementRepositoryPort {

    private final MovementJpaRepository movementJpaRepository;
    private final MovementPersistenceMapper movementMapper;

    public MovementPersistenceAdapter(MovementJpaRepository movementJpaRepository,
                                      MovementPersistenceMapper movementMapper) {
        this.movementJpaRepository = movementJpaRepository;
        this.movementMapper = movementMapper;
    }

    @Override
    public InventoryMovement save(InventoryMovement movement) {
        MovementEntity entity = movementMapper.toEntity(movement);
        MovementEntity saved = movementJpaRepository.save(entity);
        return movementMapper.toDomain(saved);
    }

    @Override
    public List<InventoryMovement> findByProductId(Long productId) {
        return movementJpaRepository.findByProductId(productId).stream()
                .map(movementMapper::toDomain)
                .toList();
    }

    @Override
    public List<InventoryMovement> findAll() {
        return movementJpaRepository.findAll().stream()
                .map(movementMapper::toDomain)
                .toList();
    }
}
