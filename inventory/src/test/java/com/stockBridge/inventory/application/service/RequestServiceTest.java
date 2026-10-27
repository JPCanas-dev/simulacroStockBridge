package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.exception.InsufficientStockException;
import com.stockBridge.inventory.domain.exception.InvalidStateTransitionException;
import com.stockBridge.inventory.domain.exception.UnauthorizedAccessException;
import com.stockBridge.inventory.domain.model.MovementType;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import com.stockBridge.inventory.domain.model.Role;
import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.domain.port.out.MovementRepositoryPort;
import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import com.stockBridge.inventory.domain.port.out.RequestRepositoryPort;
import com.stockBridge.inventory.domain.port.out.UserRepositoryPort;
import com.stockBridge.inventory.infrastructure.monitoring.InventoryMetrics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Pruebas unitarias para logica de solicitudes y entregas atomicas en RequestService
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepositoryPort requestRepository;
    @Mock
    private ProductRepositoryPort productRepository;
    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private MovementRepositoryPort movementRepository;
    @Mock
    private InventoryMetrics metrics;

    @InjectMocks
    private RequestService requestService;

    private User sampleUser;
    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleUser = new User(10L, "juan", "pass123", Role.ROLE_EMPLEADO);
        sampleProduct = new Product(1L, "MON-001", "Monitor 27", "4K", "MONITORES", BigDecimal.valueOf(300), 10, true);
        sampleRequest = new ProductRequest(100L, sampleUser, sampleProduct, 3, RequestStatus.PENDIENTE, "Solicitud para oficina", LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe crear una solicitud correctamente para producto activo")
    void createRequest_Success() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(requestRepository.save(any(ProductRequest.class))).thenAnswer(i -> i.getArgument(0));

        ProductRequest created = requestService.create(10L, 1L, 3, "Para diseño");

        assertNotNull(created);
        assertEquals(RequestStatus.PENDIENTE, created.getStatus());
        verify(metrics).incrementRequestsCreated();
    }

    @Test
    @DisplayName("Debe rechazar la creacion de solicitud si el producto esta inactivo")
    void createRequest_InactiveProduct_ThrowsInvalidStateTransitionException() {
        sampleProduct.setActive(false);
        when(userRepository.findById(10L)).thenReturn(Optional.of(sampleUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        assertThrows(InvalidStateTransitionException.class, () -> requestService.create(10L, 1L, 2, ""));
    }

    @Test
    @DisplayName("Debe aprobar solicitud pendiente cuando existe stock suficiente")
    void approveRequest_Success() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(sampleRequest));
        when(requestRepository.save(any(ProductRequest.class))).thenAnswer(i -> i.getArgument(0));

        ProductRequest approved = requestService.approve(100L);

        assertEquals(RequestStatus.APROBADA, approved.getStatus());
        verify(metrics).incrementRequestsApproved();
    }

    @Test
    @DisplayName("Debe lanzar InsufficientStockException al aprobar si no hay existencias suficientes")
    void approveRequest_NotEnoughStock_ThrowsInsufficientStockException() {
        sampleProduct.setStock(1); // Requiere 3
        when(requestRepository.findById(100L)).thenReturn(Optional.of(sampleRequest));

        assertThrows(InsufficientStockException.class, () -> requestService.approve(100L));
    }

    @Test
    @DisplayName("Debe entregar solicitud, descontar stock y registrar movimiento de salida atomicamente")
    void deliverRequest_Success() {
        sampleRequest.setStatus(RequestStatus.APROBADA);
        when(requestRepository.findById(100L)).thenReturn(Optional.of(sampleRequest));
        when(requestRepository.save(any(ProductRequest.class))).thenAnswer(i -> i.getArgument(0));

        ProductRequest delivered = requestService.deliver(100L);

        assertEquals(RequestStatus.ENTREGADA, delivered.getStatus());
        assertEquals(7, sampleProduct.getStock()); // 10 - 3 = 7
        verify(productRepository).save(sampleProduct);
        verify(movementRepository).save(any());
        verify(metrics).incrementRequestsDelivered();
    }

    @Test
    @DisplayName("Debe impedir cancelacion de solicitud a un usuario que no es el propietario")
    void cancelRequest_UnauthorizedUser_ThrowsUnauthorizedAccessException() {
        when(requestRepository.findById(100L)).thenReturn(Optional.of(sampleRequest));

        // Intenta cancelar con ID de usuario 999 distinto al propietario 10
        assertThrows(UnauthorizedAccessException.class, () -> requestService.cancel(100L, 999L));
    }
}
