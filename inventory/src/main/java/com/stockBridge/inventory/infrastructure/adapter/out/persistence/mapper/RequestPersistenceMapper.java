package com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper;

import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.RequestEntity;
import org.mapstruct.Mapper;

// Mapper MapStruct entre dominio ProductRequest y RequestEntity JPA
@Mapper(componentModel = "spring", uses = {UserPersistenceMapper.class, ProductPersistenceMapper.class})
public interface RequestPersistenceMapper {
    ProductRequest toDomain(RequestEntity entity);
    RequestEntity toEntity(ProductRequest domain);
}
