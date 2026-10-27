package com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.MovementEntity;
import org.mapstruct.Mapper;

// Mapper MapStruct entre dominio InventoryMovement y MovementEntity JPA
@Mapper(componentModel = "spring", uses = {ProductPersistenceMapper.class, RequestPersistenceMapper.class})
public interface MovementPersistenceMapper {
    InventoryMovement toDomain(MovementEntity entity);
    MovementEntity toEntity(InventoryMovement domain);
}
