package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import com.stockBridge.inventory.domain.port.in.RequestUseCase;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.CreateRequestDto;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.RejectRequestDto;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.RequestResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper.RestDtoMapper;
import com.stockBridge.inventory.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controlador para gestion y ciclo de vida de solicitudes internas
@RestController
@RequestMapping("/api/requests")
@Tag(name = "Solicitudes", description = "Endpoints para crear, tramitar y consultar solicitudes de productos")
@SecurityRequirement(name = "BearerAuth")
public class RequestController {

    private final RequestUseCase requestUseCase;
    private final RestDtoMapper restMapper;

    public RequestController(RequestUseCase requestUseCase, RestDtoMapper restMapper) {
        this.requestUseCase = requestUseCase;
        this.restMapper = restMapper;
    }

    @PostMapping
    @Operation(summary = "Crear nueva solicitud interna")
    public ResponseEntity<RequestResponse> create(
            @Valid @RequestBody CreateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProductRequest request = requestUseCase.create(
                userDetails.getId(),
                requestDto.productId(),
                requestDto.quantity(),
                requestDto.observation()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toRequestResponse(request));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Aprobar solicitud (ROLE_BODEGA, ROLE_ADMIN)")
    public ResponseEntity<RequestResponse> approve(@PathVariable Long id) {
        ProductRequest approved = requestUseCase.approve(id);
        return ResponseEntity.ok(restMapper.toRequestResponse(approved));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Rechazar solicitud con motivo (ROLE_BODEGA, ROLE_ADMIN)")
    public ResponseEntity<RequestResponse> reject(
            @PathVariable Long id,
            @Valid @RequestBody RejectRequestDto rejectDto) {

        ProductRequest rejected = requestUseCase.reject(id, rejectDto.reason());
        return ResponseEntity.ok(restMapper.toRequestResponse(rejected));
    }

    @PutMapping("/{id}/deliver")
    @Operation(summary = "Entregar solicitud y descontar inventario en transaccion (ROLE_BODEGA, ROLE_ADMIN)")
    public ResponseEntity<RequestResponse> deliver(@PathVariable Long id) {
        ProductRequest delivered = requestUseCase.deliver(id);
        return ResponseEntity.ok(restMapper.toRequestResponse(delivered));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancelar solicitud propia (ROLE_EMPLEADO)")
    public ResponseEntity<RequestResponse> cancel(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProductRequest canceled = requestUseCase.cancel(id, userDetails.getId());
        return ResponseEntity.ok(restMapper.toRequestResponse(canceled));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar solicitud por id (verificando autorizacion de propietario o rol)")
    public ResponseEntity<RequestResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdminOrBodega = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BODEGA"));

        ProductRequest request = requestUseCase.getById(id, userDetails.getId(), isAdminOrBodega);
        return ResponseEntity.ok(restMapper.toRequestResponse(request));
    }

    @GetMapping
    @Operation(summary = "Listar solicitudes (propias para empleado, globales para admin/bodega)")
    public ResponseEntity<List<RequestResponse>> getAll(
            @RequestParam(required = false) RequestStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdminOrBodega = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_BODEGA"));

        if (status != null && isAdminOrBodega) {
            return ResponseEntity.ok(restMapper.toRequestResponseList(requestUseCase.getByStatus(status)));
        }

        List<ProductRequest> list = requestUseCase.getAll(userDetails.getId(), isAdminOrBodega);
        return ResponseEntity.ok(restMapper.toRequestResponseList(list));
    }
}
