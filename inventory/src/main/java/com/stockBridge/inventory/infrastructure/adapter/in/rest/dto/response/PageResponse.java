package com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response;

import java.util.List;

// DTO de respuesta para consultas paginadas
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
}
