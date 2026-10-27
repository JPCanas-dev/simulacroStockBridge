package com.stockBridge.inventory.infrastructure.adapter.out.persistence;

import com.stockBridge.inventory.domain.model.PaginatedResult;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.entity.ProductEntity;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.mapper.ProductPersistenceMapper;
import com.stockBridge.inventory.infrastructure.adapter.out.persistence.repository.ProductJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// Adaptador de persistencia para productos con soporte de paginacion y ordenamiento
@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductPersistenceMapper productMapper;

    public ProductPersistenceAdapter(ProductJpaRepository productJpaRepository,
                                     ProductPersistenceMapper productMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity saved = productJpaRepository.save(entity);
        return productMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public Optional<Product> findByCode(String code) {
        return productJpaRepository.findByCode(code).map(productMapper::toDomain);
    }

    @Override
    public boolean existsByCode(String code) {
        return productJpaRepository.existsByCode(code);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(productMapper::toDomain)
                .toList();
    }

    @Override
    public PaginatedResult<Product> findAllPaged(int page, int size, String sortBy, String sortDir) {
        Sort sort = "desc".equalsIgnoreCase(sortDir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductEntity> entityPage = productJpaRepository.findAll(pageable);

        List<Product> content = entityPage.getContent().stream()
                .map(productMapper::toDomain)
                .toList();

        return new PaginatedResult<>(
                content,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.isFirst(),
                entityPage.isLast()
        );
    }

    @Override
    public List<Product> findByStockLessThan(int minStock) {
        return productJpaRepository.findByStockLessThan(minStock).stream()
                .map(productMapper::toDomain)
                .toList();
    }
}
