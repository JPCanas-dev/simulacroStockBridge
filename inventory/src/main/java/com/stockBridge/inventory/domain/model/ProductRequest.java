package com.stockBridge.inventory.domain.model;

import java.time.LocalDateTime;

// Modelo de dominio para solicitudes internas
public class ProductRequest {
    private Long id;
    private User user;
    private Product product;
    private Integer quantity;
    private RequestStatus status;
    private String observation;
    private LocalDateTime createdAt;

    public ProductRequest() {
    }

    public ProductRequest(Long id, User user, Product product, Integer quantity,
                          RequestStatus status, String observation, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.status = status;
        this.observation = observation;
        this.createdAt = createdAt;
    }

    // Regla: aprobar solicitud
    public void approve() {
        if (this.status != RequestStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar solicitudes en estado PENDIENTE");
        }
        this.status = RequestStatus.APROBADA;
    }

    // Regla: rechazar solicitud
    public void reject(String reason) {
        if (this.status != RequestStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar solicitudes en estado PENDIENTE");
        }
        this.status = RequestStatus.RECHAZADA;
        if (reason != null && !reason.isBlank()) {
            this.observation = this.observation == null ? reason : this.observation + " | Motivo rechazo: " + reason;
        }
    }

    // Regla: cancelar solicitud por parte del usuario
    public void cancel() {
        if (this.status != RequestStatus.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar solicitudes en estado PENDIENTE");
        }
        this.status = RequestStatus.CANCELADA;
    }

    // Regla: marcar como entregada tras salida de bodega
    public void markAsDelivered() {
        if (this.status != RequestStatus.APROBADA) {
            throw new IllegalStateException("Solo se pueden entregar solicitudes previamente APROBADAS");
        }
        this.status = RequestStatus.ENTREGADA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
