package com.stockBridge.inventory.infrastructure.adapter.in.rest;

import com.stockBridge.inventory.domain.model.PaginatedResult;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.port.in.ProductUseCase;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.request.ProductRequestDto;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.PageResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.dto.response.ProductResponse;
import com.stockBridge.inventory.infrastructure.adapter.in.rest.mapper.RestDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Controlador para gestion del catalogo de productos
@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Endpoints para administracion y consulta del catalogo")
@SecurityRequirement(name = "BearerAuth")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final RestDtoMapper restMapper;

    public ProductController(ProductUseCase productUseCase, RestDtoMapper restMapper) {
        this.productUseCase = productUseCase;
        this.restMapper = restMapper;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto (ROLE_ADMIN)")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequestDto request) {
        Product domain = restMapper.toProductDomain(request);
        Product created = productUseCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(restMapper.toProductResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente (ROLE_ADMIN)")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDto request) {
        Product domain = restMapper.toProductDomain(request);
        Product updated = productUseCase.update(id, domain);
        return ResponseEntity.ok(restMapper.toProductResponse(updated));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto por id")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        Product product = productUseCase.getById(id);
        return ResponseEntity.ok(restMapper.toProductResponse(product));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<List<ProductResponse>> getAll() {
        List<Product> list = productUseCase.getAll();
        return ResponseEntity.ok(restMapper.toProductResponseList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "Listar productos con paginacion y ordenamiento")
    public ResponseEntity<PageResponse<ProductResponse>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PaginatedResult<Product> result = productUseCase.getAllPaged(page, size, sortBy, sortDir);
        List<ProductResponse> dtos = restMapper.toProductResponseList(result.content());

        PageResponse<ProductResponse> response = new PageResponse<>(
                dtos,
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages(),
                result.first(),
                result.last()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Consultar productos con existencias inferiores al minimo")
    public ResponseEntity<List<ProductResponse>> getLowStock(@RequestParam(defaultValue = "5") int minStock) {
        List<Product> list = productUseCase.getLowStock(minStock);
        return ResponseEntity.ok(restMapper.toProductResponseList(list));
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Activar o desactivar producto (ROLE_ADMIN)")
    public ResponseEntity<ProductResponse> toggleActive(@PathVariable Long id) {
        Product product = productUseCase.toggleActive(id);
        return ResponseEntity.ok(restMapper.toProductResponse(product));
    }
}
