package com.stockBridge.inventory.domain.model;

import java.util.List;

// Record de paginacion agnostico a frameworks de infraestructura
public record PaginatedResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
