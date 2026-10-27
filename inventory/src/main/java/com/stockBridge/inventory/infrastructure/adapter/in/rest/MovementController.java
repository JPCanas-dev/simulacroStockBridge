package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.domain.port.in.MovementUseCase;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.MovementResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper.RestDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controlador para consulta de movimientos y trazabilidad de inventario
@RestController
@RequestMapping("/api/movements")
@Tag(name = "Movimientos", description = "Endpoints para trazabilidad de entradas y salidas de bodega (ROLE_BODEGA, ROLE_ADMIN)")
@SecurityRequirement(name = "BearerAuth")
public class MovementController {

    private final MovementUseCase movementUseCase;
    private final RestDtoMapper restMapper;

    public MovementController(MovementUseCase movementUseCase, RestDtoMapper restMapper) {
        this.movementUseCase = movementUseCase;
        this.restMapper = restMapper;
    }

    @GetMapping
    @Operation(summary = "Listar movimientos de inventario (opcional filtrar por producto)")
    public ResponseEntity<List<MovementResponse>> getAll(@RequestParam(required = false) Long productId) {
        List<InventoryMovement> list = productId != null
                ? movementUseCase.getByProductId(productId)
                : movementUseCase.getAll();
        return ResponseEntity.ok(restMapper.toMovementResponseList(list));
    }
}
