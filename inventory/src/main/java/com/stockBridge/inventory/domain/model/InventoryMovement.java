package com.stockBridge.inventory.domain.model;

import java.time.LocalDateTime;

// Modelo de dominio para movimientos de inventario
public class InventoryMovement {
    private Long id;
    private Product product;
    private ProductRequest request;
    private MovementType movementType;
    private Integer quantity;
    private LocalDateTime createdAt;

    public InventoryMovement() {
    }

    public InventoryMovement(Long id, Product product, ProductRequest request,
                             MovementType movementType, Integer quantity, LocalDateTime createdAt) {
        this.id = id;
        this.product = product;
        this.request = request;
        this.movementType = movementType;
        this.quantity = quantity;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductRequest getRequest() {
        return request;
    }

    public void setRequest(ProductRequest request) {
        this.request = request;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
