package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.exception.InsufficientStockException;
import com.stockBridge.inventory.domain.exception.InvalidStateTransitionException;
import com.stockBridge.inventory.domain.exception.ResourceNotFoundException;
import com.stockBridge.inventory.domain.exception.UnauthorizedAccessException;
import com.stockBridge.inventory.domain.model.InventoryMovement;
import com.stockBridge.inventory.domain.model.MovementType;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import com.stockBridge.inventory.domain.model.User;
import com.stockBridge.inventory.domain.port.in.RequestUseCase;
import com.stockBridge.inventory.domain.port.out.MovementRepositoryPort;
import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import com.stockBridge.inventory.domain.port.out.RequestRepositoryPort;
import com.stockBridge.inventory.domain.port.out.UserRepositoryPort;
import com.stockBridge.inventory.infrastructure.monitoring.InventoryMetrics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Implementacion de casos de uso de solicitudes internas
@Service
@Transactional
public class RequestService implements RequestUseCase {

    private final RequestRepositoryPort requestRepository;
    private final ProductRepositoryPort productRepository;
    private final UserRepositoryPort userRepository;
    private final MovementRepositoryPort movementRepository;
    private final InventoryMetrics metrics;

    public RequestService(RequestRepositoryPort requestRepository,
                          ProductRepositoryPort productRepository,
                          UserRepositoryPort userRepository,
                          MovementRepositoryPort movementRepository,
                          InventoryMetrics metrics) {
        this.requestRepository = requestRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.movementRepository = movementRepository;
        this.metrics = metrics;
    }

    @Override
    public ProductRequest create(Long userId, Long productId, int quantity, String observation) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad requerida debe ser mayor a cero");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new InvalidStateTransitionException("No se pueden solicitar productos inactivos");
        }

        ProductRequest request = new ProductRequest();
        request.setUser(user);
        request.setProduct(product);
        request.setQuantity(quantity);
        request.setStatus(RequestStatus.PENDIENTE);
        request.setObservation(observation);
        request.setCreatedAt(LocalDateTime.now());

        ProductRequest saved = requestRepository.save(request);
        metrics.incrementRequestsCreated();
        return saved;
    }

    @Override
    public ProductRequest approve(Long requestId) {
        ProductRequest request = getRequest(requestId);

        if (request.getStatus() != RequestStatus.PENDIENTE) {
            throw new InvalidStateTransitionException("Solo se pueden aprobar solicitudes en estado PENDIENTE");
        }

        Product product = request.getProduct();
        if (!product.hasStockAvailable(request.getQuantity())) {
            throw new InsufficientStockException("Stock insuficiente para aprobar la solicitud. Disponible: "
                    + product.getStock() + ", requerido: " + request.getQuantity());
        }

        request.approve();
        ProductRequest saved = requestRepository.save(request);
        metrics.incrementRequestsApproved();
        return saved;
    }

    @Override
    public ProductRequest reject(Long requestId, String reason) {
        ProductRequest request = getRequest(requestId);

        if (request.getStatus() != RequestStatus.PENDIENTE) {
            throw new InvalidStateTransitionException("Solo se pueden rechazar solicitudes en estado PENDIENTE");
        }

        request.reject(reason);
        ProductRequest saved = requestRepository.save(request);
        metrics.incrementRequestsRejected();
        return saved;
    }

    @Override
    public ProductRequest deliver(Long requestId) {
        ProductRequest request = getRequest(requestId);

        if (request.getStatus() != RequestStatus.APROBADA) {
            throw new InvalidStateTransitionException("Solo se pueden entregar solicitudes en estado APROBADA");
        }

        Product product = request.getProduct();
        if (!product.hasStockAvailable(request.getQuantity())) {
            throw new InsufficientStockException("Inventario insuficiente al momento de la entrega. Disponible: "
                    + product.getStock() + ", requerido: " + request.getQuantity());
        }

        // 1. Descontar unidades del producto
        product.deductStock(request.getQuantity());
        productRepository.save(product);

        // 2. Transicion de estado de la solicitud
        request.markAsDelivered();
        ProductRequest updatedRequest = requestRepository.save(request);

        // 3. Registrar movimiento de inventario en la misma transaccion
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setRequest(updatedRequest);
        movement.setMovementType(MovementType.SALIDA);
        movement.setQuantity(request.getQuantity());
        movement.setCreatedAt(LocalDateTime.now());
        movementRepository.save(movement);

        metrics.incrementRequestsDelivered();
        return updatedRequest;
    }

    @Override
    public ProductRequest cancel(Long requestId, Long currentUserId) {
        ProductRequest request = getRequest(requestId);

        // Solo el solicitante puede cancelar su solicitud
        if (!request.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("No tienes permiso para cancelar esta solicitud");
        }

        if (request.getStatus() != RequestStatus.PENDIENTE) {
            throw new InvalidStateTransitionException("Solo se pueden cancelar solicitudes en estado PENDIENTE");
        }

        request.cancel();
        return requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductRequest getById(Long id, Long currentUserId, boolean isAdminOrBodega) {
        ProductRequest request = getRequest(id);

        if (!isAdminOrBodega && !request.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("No tienes permiso para consultar esta solicitud");
        }

        return request;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductRequest> getAll(Long currentUserId, boolean isAdminOrBodega) {
        if (isAdminOrBodega) {
            return requestRepository.findAll();
        }
        return requestRepository.findByUserId(currentUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductRequest> getByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    private ProductRequest getRequest(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));
    }
}
