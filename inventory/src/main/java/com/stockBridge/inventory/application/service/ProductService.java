package com.stockBridge.inventory.application.service;

import com.stockBridge.inventory.domain.exception.DuplicateResourceException;
import com.stockBridge.inventory.domain.exception.ResourceNotFoundException;
import com.stockBridge.inventory.domain.model.PaginatedResult;
import com.stockBridge.inventory.domain.model.Product;
import com.stockBridge.inventory.domain.port.in.ProductUseCase;
import com.stockBridge.inventory.domain.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Implementacion de casos de uso de productos
@Service
@Transactional
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepository;

    public ProductService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        if (productRepository.existsByCode(product.getCode())) {
            throw new DuplicateResourceException("Ya existe un producto con el codigo: " + product.getCode());
        }
        validateProduct(product);
        product.setActive(true);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product updatedProduct) {
        Product current = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        // Si cambia el codigo, verificar que no este tomado por otro
        if (!current.getCode().equalsIgnoreCase(updatedProduct.getCode()) &&
                productRepository.existsByCode(updatedProduct.getCode())) {
            throw new DuplicateResourceException("El codigo ya esta en uso: " + updatedProduct.getCode());
        }

        validateProduct(updatedProduct);

        current.setCode(updatedProduct.getCode());
        current.setName(updatedProduct.getName());
        current.setDescription(updatedProduct.getDescription());
        current.setCategory(updatedProduct.getCategory());
        current.setPrice(updatedProduct.getPrice());
        current.setStock(updatedProduct.getStock());

        return productRepository.save(current);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResult<Product> getAllPaged(int page, int size, String sortBy, String sortDir) {
        return productRepository.findAllPaged(page, size, sortBy, sortDir);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStock(int minStock) {
        return productRepository.findByStockLessThan(minStock);
    }

    @Override
    public Product toggleActive(Long id) {
        Product product = getById(id);
        product.setActive(!Boolean.TRUE.equals(product.getActive()));
        return productRepository.save(product);
    }

    private void validateProduct(Product product) {
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (product.getStock() == null || product.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }
}
