package com.stockBridge.inventory.domain.port.in;

import com.stockBridge.inventory.domain.model.PaginatedResult;
import com.stockBridge.inventory.domain.model.Product;
import java.util.List;

// Casos de uso de productos con paginacion y ordenamiento
public interface ProductUseCase {
    Product create(Product product);
    Product update(Long id, Product product);
    Product getById(Long id);
    List<Product> getAll();
    PaginatedResult<Product> getAllPaged(int page, int size, String sortBy, String sortDir);
    List<Product> getLowStock(int minStock);
    Product toggleActive(Long id);
}
