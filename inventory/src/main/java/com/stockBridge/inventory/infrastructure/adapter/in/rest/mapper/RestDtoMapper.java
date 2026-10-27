package com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.SupplierAvailability;
import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.ProductRequestDto;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.AvailabilityResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.MovementResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.ProductResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.RequestResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// Mapper MapStruct para transformar entre modelos de dominio y DTOs REST
@Mapper(componentModel = "spring")
public interface RestDtoMapper {

    UserResponse toUserResponse(User domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    Product toProductDomain(ProductRequestDto dto);

    ProductResponse toProductResponse(Product domain);

    List<ProductResponse> toProductResponseList(List<Product> domainList);

    RequestResponse toRequestResponse(ProductRequest domain);

    List<RequestResponse> toRequestResponseList(List<ProductRequest> domainList);

    @Mapping(target = "requestId", source = "request.id")
    MovementResponse toMovementResponse(InventoryMovement domain);

    List<MovementResponse> toMovementResponseList(List<InventoryMovement> domainList);

    AvailabilityResponse toAvailabilityResponse(SupplierAvailability domain);
}
