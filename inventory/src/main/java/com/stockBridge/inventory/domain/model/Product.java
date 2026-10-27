package com.stockBridge.inventory.domain.model;

import java.math.BigDecimal;

// Modelo de dominio para productos
public class Product {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;

    public Product() {
    }

    public Product(Long id, String code, String name, String description, String category,
                   BigDecimal price, Integer stock, Boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.active = active;
    }

    // Regla: verificar si tiene existencias suficientes
    public boolean hasStockAvailable(int quantity) {
        return this.stock != null && this.stock >= quantity;
    }

    // Regla: descontar stock
    public void deductStock(int quantity) {
        if (!hasStockAvailable(quantity)) {
            throw new IllegalStateException("Stock insuficiente para el producto: " + this.code);
        }
        this.stock -= quantity;
    }

    // Regla: agregar stock
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a sumar debe ser positiva");
        }
        this.stock = (this.stock == null ? 0 : this.stock) + quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
