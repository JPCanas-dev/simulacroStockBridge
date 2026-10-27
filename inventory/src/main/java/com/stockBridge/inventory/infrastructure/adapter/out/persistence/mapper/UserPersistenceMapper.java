package com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper;

import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Mapper MapStruct entre dominio User y UserEntity JPA
@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {
    User toDomain(UserEntity entity);

    @Mapping(target = "requests", ignore = true)
    UserEntity toEntity(User domain);
}
