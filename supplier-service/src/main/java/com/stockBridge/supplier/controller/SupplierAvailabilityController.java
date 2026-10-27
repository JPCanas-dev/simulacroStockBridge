package com.stockBridge.supplier.controller;

import com.stockBridge.supplier.dto.AvailabilityCheckRequest;
import com.stockBridge.supplier.dto.AvailabilityCheckResponse;
import com.stockBridge.supplier.service.SupplierSimulatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST del simulador de proveedores externos
@RestController
@RequestMapping("/api/suppliers")
public class SupplierAvailabilityController {

    private final SupplierSimulatorService simulatorService;

    public SupplierAvailabilityController(SupplierSimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @PostMapping("/check-availability")
    public ResponseEntity<AvailabilityCheckResponse> checkAvailability(
            @Valid @RequestBody AvailabilityCheckRequest request) {

        AvailabilityCheckResponse response = simulatorService.check(request);
        return ResponseEntity.ok(response);
    }
}
