package com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper;

import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Mapper MapStruct entre dominio Product y ProductEntity JPA
@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {
    Product toDomain(ProductEntity entity);

    @Mapping(target = "movements", ignore = true)
    ProductEntity toEntity(Product domain);
}
