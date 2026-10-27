package com.stockBridge.inventory.infrastructure.adapter.out.persistence;

import com.stockBridge.inventory.domain.model.ProductRequest;
import com.stockBridge.inventory.domain.model.RequestStatus;
import com.stockBridge.inventory.domain.port.out.RequestRepositoryPort;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.RequestEntity;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper.RequestPersistenceMapper;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository.RequestJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// Adaptador de persistencia para solicitudes internas
@Component
public class RequestPersistenceAdapter implements RequestRepositoryPort {

    private final RequestJpaRepository requestJpaRepository;
    private final RequestPersistenceMapper requestMapper;

    public RequestPersistenceAdapter(RequestJpaRepository requestJpaRepository,
                                     RequestPersistenceMapper requestMapper) {
        this.requestJpaRepository = requestJpaRepository;
        this.requestMapper = requestMapper;
    }

    @Override
    public ProductRequest save(ProductRequest request) {
        RequestEntity entity = requestMapper.toEntity(request);
        RequestEntity saved = requestJpaRepository.save(entity);
        return requestMapper.toDomain(saved);
    }

    @Override
    public Optional<ProductRequest> findById(Long id) {
        return requestJpaRepository.findById(id).map(requestMapper::toDomain);
    }

    @Override
    public List<ProductRequest> findAll() {
        return requestJpaRepository.findAll().stream()
                .map(requestMapper::toDomain)
                .toList();
    }

    @Override
    public List<ProductRequest> findByUserId(Long userId) {
        return requestJpaRepository.findByUserId(userId).stream()
                .map(requestMapper::toDomain)
                .toList();
    }

    @Override
    public List<ProductRequest> findByStatus(RequestStatus status) {
        return requestJpaRepository.findByStatus(status).stream()
                .map(requestMapper::toDomain)
                .toList();
    }
}
