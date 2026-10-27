package com.stockBridge.inventory.domain.port.out;

import com.stockBridge.inventory.domain.model.PaginatedResult;
import com.stockBridge.inventory.domain.model.Product;
import java.util.List;
import java.util.Optional;

// Puerto de salida para persistencia de productos con soporte de paginacion
public interface ProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findByCode(String code);
    boolean existsByCode(String code);
    List<Product> findAll();
    PaginatedResult<Product> findAllPaged(int page, int size, String sortBy, String sortDir);
    List<Product> findByStockLessThan(int minStock);
}
