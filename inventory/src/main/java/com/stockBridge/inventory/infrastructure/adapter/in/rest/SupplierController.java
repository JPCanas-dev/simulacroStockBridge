package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.SupplierAvailability;
import com.stockBridge.inventory.domain.port.in.SupplierAvailabilityUseCase;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.CheckAvailabilityRequest;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.AvailabilityResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper.RestDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador para consultar abastecimiento con el servicio externo de proveedores
@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Proveedores", description = "Endpoints de integracion con el servicio de proveedores externos")
@SecurityRequirement(name = "BearerAuth")
public class SupplierController {

    private final SupplierAvailabilityUseCase supplierUseCase;
    private final RestDtoMapper restMapper;

    public SupplierController(SupplierAvailabilityUseCase supplierUseCase, RestDtoMapper restMapper) {
        this.supplierUseCase = supplierUseCase;
        this.restMapper = restMapper;
    }

    @PostMapping("/check-availability")
    @Operation(summary = "Consultar disponibilidad de producto a proveedor externo")
    public ResponseEntity<AvailabilityResponse> checkAvailability(@Valid @RequestBody CheckAvailabilityRequest request) {
        SupplierAvailability availability = supplierUseCase.checkAvailability(
                request.productCode(),
                request.quantity(),
                request.category()
        );
        return ResponseEntity.ok(restMapper.toAvailabilityResponse(availability));
    }
}
